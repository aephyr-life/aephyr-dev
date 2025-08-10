package aephyr.identity.application

import aephyr.config.MagicLinkCfg
import aephyr.identity.domain.User
import aephyr.identity.domain.auth.AuthError
import aephyr.identity.application.ports.*
import aephyr.shared.security.SecureRandom
import zio.{Trace, UIO, ZIO, ZLayer}
import zio.*

import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

final case class MagicLinkServiceLive(
                                       tokens: TokenStore,
                                       email: EmailSender,
                                       usersRead: UserReadPort,
                                       usersWrite: UserWritePort,
                                       cfg: MagicLinkCfg,
                                       clock: Clock,
                                       random: SecureRandom
                                     ) extends MagicLinkService:

  private def nowInstant: UIO[Instant] =
    clock.instant

  private def randToken(bytes: Int = 32): UIO[String] =
    random.nextBytes(bytes).map { result =>
      Base64.getUrlEncoder
        .withoutPadding
        .encodeToString(result)
    }

  private def hmacSha256Base64Url(hmacKey: SecretKeySpec, token: String): Task[String] =
    ZIO.attempt {
      val mac = Mac.getInstance("HmacSHA256")
      mac.init(hmacKey)
      val raw = mac.doFinal(token.getBytes("UTF-8"))
      Base64.getUrlEncoder
        .withoutPadding
        .encodeToString(raw)
    }

  /*
    TODO
    - Rate-Limit pro email/ip (z. B. 5–10/h), ggf. Captcha für Public-Traffic.
    - Single-Use Token, serverseitig gehasht (HMAC-Secret passt ja jetzt 😉).
    - Transaktionen bei create user + create token.
    - Idempotenz: upsertPending(email) muss race-safe sein.
  */
  override def sendMagicLink(reqEmail: User.EmailAddress, clientIp: String, ua: String)(using Trace): UIO[Unit] = {
    val emailNorm = reqEmail.normalized
    (for
      now <- nowInstant
      uid <- usersWrite.createPending(emailNorm, now)
      token <- randToken(32)
      hash <- hmacSha256Base64Url(getSecretKey(cfg.hmacSecretB64Url), token).orDie
      _ <- tokens.put(
        hash,
        uid,
        "login",
        now.plusSeconds(cfg.ttlMinutes * 60), // TODO use duration here
        singleUse = true
      ) // TODO don't hardcode strings
      link = s"${cfg.baseUrl}/auth/callback?token=$token" // TODO get URL from endpoints
      body = s"Klicke zum Anmelden: $link\nDieser Link ist ${cfg.ttlMinutes} Minuten gültig." // TODO get this text from somewhere else and use duration
      _ <- email.send(emailNorm, "Your login link", s"<p>${body}</p>", body) // TODO also this text should come from a template
    yield ()
  ).ignore
  } // returns always >ok< (no user enumeration).

  override def consumeMagicLink(token: String): IO[AuthError, User] =
    for {
      now <- nowInstant
      hash <- hmacSha256Base64Url(getSecretKey(cfg.hmacSecretB64Url), token)
        .mapError(AuthError.Internal.apply)
      rec <- tokens.consumeSingleUse(hash, now)
        .mapError(AuthError.Internal.apply)
      user <- usersRead.findById(rec.userId)
        .someOrFail(AuthError.InvalidToken)
        .mapError(AuthError.Internal.apply)
      _ <- user.status match {
        case User.Status.Pending  => usersWrite
          .activate(user.id, now)
          .mapError(AuthError.Internal.apply)
        case User.Status.Active   => ZIO.unit
        case User.Status.Disabled => ZIO.fail(AuthError.DisabledUser)
      }
      _ <- usersWrite
        .touchLastLogin(user.id, now)
        .mapError(AuthError.Internal.apply)
    } yield user

def getSecretKey(s: String): SecretKeySpec =
  ???


object MagicLinkServiceLive:

  val layer: ZLayer[TokenStore & EmailSender & UserReadPort & UserWritePort & MagicLinkCfg & Clock & SecureRandom, Nothing, MagicLinkService] =
    ZLayer.fromFunction(MagicLinkServiceLive.apply)

