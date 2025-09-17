package aephyr.auth.application.webauthn

import zio.*
import aephyr.auth.domain.{AuthTx, Credential, UserHandle}
import aephyr.auth.domain.webauthn.*
import aephyr.auth.ports.*
import aephyr.kernel.id.UserId
import WebAuthnService.*
import aephyr.kernel.ObjectOps.*
import aephyr.kernel.userlabels.UserLabels

final class WebAuthnServiceLive(
  platform: WebAuthnPlatform,
  tx: ChallengeStore,
  handles: UserHandleRepo,
  repo: WebAuthnRepo,
  clock: Clock
) extends WebAuthnService {

  private inline def msg(th: Throwable): Option[String] =
    Option(th.getMessage).map(_.nn)

  private def ensureUserHandle(userId: UserId): IO[WebAuthnError, UserHandle] =
    handles
      .get(userId)
      .mapError(
        e => WebAuthnError.Server(e.getMessage.option)
      )
      .flatMap {
        case Some(h) => ZIO.succeed(h)
        case None =>
          for {
            bytes <- Random.nextBytes(32).map(_.toArray)
            h = UserHandle(bytes)
            _ <- handles
              .put(userId, h)
              .mapError(
                e => WebAuthnError.Server(e.getMessage.option)
              )
          } yield h
      }

  // ---------- Registration ----------

  def registrationOptions(): IO[WebAuthnError, BeginRegResult] =
    for {
      provisionalUserId <- ZIO.succeed(UserId.random)
      (userName, displayName) = UserLabels.from(provisionalUserId)
      uh <- ensureUserHandle(provisionalUserId)
      user = UserEntity(id = uh, name = userName, displayName = displayName)
      res <- platform
        .startRegistration(user)
        .mapError(
          e => WebAuthnError.Server(e.getMessage.option)
        )
      txId <- tx.putReg(uh.bytes, res.serverJson)
    } yield BeginRegResult(txId, res.clientJson)

  import aephyr.kernel.types.Bytes

  override def registrationVerify(
    authTx: AuthTx,
    json: String
  ): IO[WebAuthnError, RegistrationVerified] =
    tx.getReg(authTx)
      .someOrFail(
        WebAuthnError.InvalidTx
      )
      .mapError(identity[WebAuthnError])
      .flatMap {
        case (uhBytes: Bytes, creationJson: String) =>
          for {
            userId <- handles
              .findByHandle(UserHandle(uhBytes.toArray))
              .mapError(
                e => WebAuthnError.Server(e.getMessage.option)
              ) // Throwable -> WebAuthnError
              .someOrFail(WebAuthnError.Server(Some("user_handle_unmapped")))

            fin <- platform
              .finishRegistration(creationJson, json)
              .mapError(
                th => WebAuthnError.Server(msg(th))
              )

            now <- clock.instant
            cred = Credential(
              id = java.util.UUID.randomUUID().nn,
              userId = userId,
              credentialId = CredentialId(fin.credentialId).toOption.get,
              publicKeyCose = PublicKeyCose(fin.publicKeyCose).toOption.get,
              signCount = fin.signCount,
              userHandle = UserHandle(uhBytes),
              uvRequired = false,
              transports = fin.transports,
              label = None,
              createdAt = now,
              updatedAt = now,
              lastUsedAt = None
            )

            _ <- repo
              .insert(cred)
              .mapError(
                th => WebAuthnError.Server(msg(th))
              )
            _ <- tx.delReg(authTx).ignore
          } yield RegistrationVerified(userId = userId)
      }

  override def authenticationOptions(): IO[WebAuthnError, BeginAuthResult] =
    for {
      start <- platform
        .startAssertion()
        .mapError(e => WebAuthnError.Server(msg(e)))
      txId <- tx
        .putAuth(start.serverJson)
    } yield BeginAuthResult(txId, start.clientJson)

  override def authenticationVerify(
                                     authTx: AuthTx,
                                     json: String
                                   ): IO[WebAuthnError, AuthenticationVerified] = {
    def toServerErr(th: Throwable): WebAuthnError = WebAuthnError.Server(msg(th))

    (for {
      serverJson <- tx.getAuth(authTx).someOrFail(WebAuthnError.InvalidTx)
      fin <- platform.finishAssertion(serverJson, json).mapError(toServerErr)
      credId <- ZIO
        .fromEither(CredentialId(fin.credentialId))
        .mapError(m => WebAuthnError.Server(Some(m)))

      userId <- fin.userHandle match {
        case Some(uhBytes: Bytes) =>
          handles
            .findByHandle(UserHandle(uhBytes.toArray))
            .mapError(toServerErr)
            .someOrFail(WebAuthnError.Server(Some("user_handle_unmapped")))
        case None =>
          repo
            .findByCredentialId(credId)
            .mapError(toServerErr)
            .someOrFail(WebAuthnError.Server(Some("credential_not_found")))
            .map(_.userId)
      }

      now <- clock.instant
      _ <- repo
        .updateUsage(credId, fin.signCount, now)
        .mapError(toServerErr)

    } yield AuthenticationVerified(userId))
      .ensuring(tx.delAuth(authTx).ignore)
      .tapError(e => ZIO.logWarning(s"webauthn authenticationVerify failed tx=$authTx: $e"))
  }
}

object WebAuthnServiceLive {
  val layer: ZLayer[
    WebAuthnPlatform & ChallengeStore & UserHandleRepo & WebAuthnRepo & Clock,
    Nothing,
    WebAuthnService
  ] =
    ZLayer.fromFunction(WebAuthnServiceLive(_, _, _, _, _))
}
