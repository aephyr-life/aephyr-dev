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

  def beginRegistration(cmd: BeginRegCmd): IO[WebAuthnError, BeginRegResult] =
    for {
      uh <- ensureUserHandle(cmd.userId)
      user = UserEntity(handle = uh, name = cmd.username, displayName = cmd.displayName)

      // typed options + raw JSON that must be stored for finishRegistration()
      tuple <- platform
        .startRegistration(user, cmd.authenticatorSelection, cmd.attestation, cmd.excludeCredentials)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
      (options, platformJson) = tuple

      txId <- tx.putReg(uh.bytes, platformJson)
    } yield BeginRegResult(txId, options)

  def finishRegistration(cmd: FinishRegCmd): IO[WebAuthnError, Unit] =
    for {
      tuple <- tx.getReg(cmd.tx).someOrFail(WebAuthnError.InvalidTx)
      (uhBytes, reqJson) = tuple

      // verify using typed response
      tuple <- platform
        .finishRegistration(reqJson, cmd.response)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
      (credId, pkCose, signCount, handle) = tuple

      userId <- handles
        .findByHandle(handle)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
        .someOrFail(WebAuthnError.Server("no user for handle"))

      now <- clock.instant
      cred = Credential(
        id = java.util.UUID.randomUUID().nn,
        userId = userId,
        credentialId = credId.bytes,
        publicKeyCose = pkCose.bytes,
        signCount = signCount,
        userHandleBytes = uhBytes,
        uvRequired = false,
        transports = Nil,
        label = cmd.label,
        createdAt = now,
        updatedAt = now,
        lastUsedAt = None
      )

      _ <- repo.insert(cred).mapError(e => WebAuthnError.Server(e.getMessage.option))
      _ <- tx.delReg(cmd.tx).ignore
    } yield ()

  // ---------- Authentication ----------

  def beginAuthentication(cmd: BeginAuthCmd): IO[WebAuthnError, BeginAuthResult] =
    for {
      tuple <- platform
        .startAssertion(cmd.userVerification, cmd.allowCredentials)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
      (options, platformJson) = tuple

      txId <- tx.putAuth(platformJson)
    } yield BeginAuthResult(txId, options)

  def finishAuthentication(cmd: FinishAuthCmd): IO[WebAuthnError, Unit] =
    for {
      reqJson <- tx.getAuth(cmd.tx).someOrFail(WebAuthnError.InvalidTx)

      tuple <- platform
        .finishAssertion(reqJson, cmd.response)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
      (credId, newCount) = tuple

      _ <- repo
        .updateSignCount(credId.bytes.toArray, newCount)
        .mapError(e => WebAuthnError.Server(e.getMessage.option))
      _ <- tx.delAuth(cmd.tx).ignore
    } yield ()
}

object WebAuthnServiceLive {
  val layer: ZLayer[
    WebAuthnPlatform & ChallengeStore & UserHandleRepo & WebAuthnRepo & Clock,
    Nothing,
    WebAuthnService
  ] =
    ZLayer.fromFunction(WebAuthnServiceLive(_, _, _, _, _))
}
