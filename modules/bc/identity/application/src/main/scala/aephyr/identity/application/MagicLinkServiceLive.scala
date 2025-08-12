package aephyr.identity.application

import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import aephyr.identity.application.ports._
import aephyr.identity.domain.User
import aephyr.identity.domain.auth.{ AuthError, TokenStoreError }
import aephyr.kernel.DurationOps._
import aephyr.shared.config.MagicLinkIssuance
import aephyr.shared.security.SecureRandom
import zio._
import zio.logging._

// TODO add cooldown per email/IP (1 link / 60s, 5 / hour)
final case class MagicLinkServiceLive(
  tokens: TokenStore,
  email: EmailSender,
  usersRead: UserReadPort,
  usersWrite: UserWritePort,
  cfg: MagicLinkIssuance,
  clock: Clock,
  random: SecureRandom,
  createMagicLink: String => String
) extends MagicLinkService:

  private def nowInstant: UIO[Instant] = clock.instant

  private def hmacSha256Base64Url(
    hmacKey: SecretKeySpec,
    token: String
  ): Task[String] =
    ZIO.attempt {
      val mac = Mac.getInstance("HmacSHA256").nn
      mac.init(hmacKey)
      val raw = mac.doFinal(token.getBytes("UTF-8")).nn
      Base64.getUrlEncoder.nn.withoutPadding.nn
        .encodeToString(raw)
        .nn
    }

  /*
    returns always >ok< (no user enumeration).
   */
  override def sendMagicLink(
    reqEmail: User.EmailAddress,
    clientIp: String,
    ua: String
  )(using Trace): UIO[Unit] = {
    val emailNorm = reqEmail.normalized
    (for
      now   <- nowInstant
      uid   <- usersWrite.createPending(emailNorm, now)
      token <- random.nextBytesAsBase64(32)
      hash  <- hmacSha256Base64Url(cfg.hmacSecretB64Url, token).orDie
      _ <- tokens.put(
        hash,
        uid,
        "login",
        now.plusSeconds(cfg.ttl.getSeconds).nn,
        singleUse = true
      ) // TODO don't hardcode strings
      link = createMagicLink(token)
      body =
        s"Klicke zum Anmelden: $link\nDieser Link ist ${cfg.ttl.minutes} Minuten gültig." // TODO get this text from somewhere else and use duration
      _ <- email.send(
        emailNorm,
        "Your login link",
        s"<p>${body}</p>",
        body
      ) // TODO also this text should come from a template
    yield ()).catchAllCause {
      cause =>
        ZIO.logErrorCause(s"sendMagicLink failed.", cause)
    }
  } @@ loggerName("app.email")

  override def consumeMagicLink(token: String): IO[AuthError, User] =
    for {
      now <- nowInstant
      hash <- hmacSha256Base64Url(cfg.hmacSecretB64Url, token)
        .mapError(
          e =>
            e.printStackTrace()
            AuthError.Internal.apply(e)
        ) // TODO don't leak internal data
      rec <- tokens
        .consumeSingleUse(hash, now)
        .mapError {
          case TokenStoreError.InvalidOrExpired => AuthError.InvalidToken
          case e =>
            e.printStackTrace()
            AuthError.Internal(e)
        }
      user <- usersRead
        .findById(rec.userId)
        .mapError(
          e =>
            e.printStackTrace()
            AuthError.Internal.apply(e) // TODO don't leak internal data
        )
        .someOrFail {
          println("did not find user: " + rec.userId)
          AuthError.InvalidToken
        }
      _ <- user.status match {
        case User.Status.Pending =>
          usersWrite
            .activate(user.id, now)
            .mapError(
              e => AuthError.Internal.apply(e)
            )
        case User.Status.Active   => ZIO.unit
        case User.Status.Disabled => ZIO.fail(AuthError.DisabledUser)
      }
      _ <- usersWrite
        .touchLastLogin(user.id, now)
        .mapError(AuthError.Internal.apply) // TODO don't leak internal data
    } yield user

object MagicLinkServiceLive:

  val layer: ZLayer[
    TokenStore & EmailSender & UserReadPort & UserWritePort &
      MagicLinkIssuance & Clock & SecureRandom & (String => String),
    Nothing,
    MagicLinkService
  ] =
    ZLayer.fromFunction(MagicLinkServiceLive.apply)
