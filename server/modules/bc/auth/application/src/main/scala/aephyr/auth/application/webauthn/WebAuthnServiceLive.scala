package aephyr.auth.application.webauthn

import zio.*
import aephyr.auth.domain.UserHandle
import aephyr.auth.domain.Credential
import aephyr.auth.domain.webauthn.*
import aephyr.auth.ports.*
import aephyr.kernel.id.UserId
import WebAuthnService.*
import aephyr.kernel.ObjectOps.*

final class WebAuthnServiceLive(
                                 platform: WebAuthnPlatform,
                                 tx: ChallengeStore,
                                 handles: UserHandleRepo,
                                 repo: WebAuthnRepo,
                                 clock: Clock
                               ) extends WebAuthnService {

  private def ensureUserHandle(userId: UserId): IO[WebAuthnError, UserHandle] =
    handles.get(userId).mapError(e => WebAuthnError.Server(e.getMessage.option)).flatMap {
      case Some(h) => ZIO.succeed(h)
      case None =>
        for {
          bytes <- Random.nextBytes(32).map(_.toArray)
          h = UserHandle(bytes)
          _ <- handles.put(userId, h).mapError(e => WebAuthnError.Server(e.getMessage.option))
        } yield h
    }

  // ---------- Registration ----------

  def registrationOptions(): IO[WebAuthnError, BeginRegResult] =
    for {
      provisionalUserId <- ZIO.succeed(UserId.random)
      uh <- ensureUserHandle(provisionalUserId)
      username <- ZIO.succeed(s"user-${provisionalUserId.value.toString.take(8)}")
      displayName <- ZIO.succeed("New User")
      user = UserEntity(id = uh, name = username, displayName = displayName)
      res <- platform.startRegistration(user)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
      txId <- tx.putReg(uh.bytes, res.serverJson)
    } yield BeginRegResult(txId, res.clientJson)
}

object WebAuthnServiceLive {
  val layer: ZLayer[
    WebAuthnPlatform & ChallengeStore & UserHandleRepo & WebAuthnRepo & Clock,
    Nothing,
    WebAuthnService
  ] =
    ZLayer.fromFunction(WebAuthnServiceLive(_, _, _, _, _))
}
