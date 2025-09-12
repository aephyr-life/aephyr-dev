package aephyr.auth.application.webauthn

import aephyr.auth.domain.AuthTx
import aephyr.auth.domain.webauthn.*
import aephyr.kernel.id.UserId

trait WebAuthnService {
  import zio.IO
  import WebAuthnService.*

  def registrationOptions(): IO[WebAuthnError, BeginRegResult]

  def registrationVerify(tx: AuthTx, json: String): IO[WebAuthnError, RegistrationVerified]
//  def finishRegistration(cmd: FinishRegCmd): IO[WebAuthnError, Unit]
//
//  def beginAuthentication(cmd: BeginAuthCmd): IO[WebAuthnError, BeginAuthResult]
//  def finishAuthentication(cmd: FinishAuthCmd): IO[WebAuthnError, Unit]
}

object WebAuthnService {

  // ---------- Results ----------
  final case class BeginRegResult(
    tx: AuthTx,
    clientJson: String
  )

  final case class RegistrationVerified(
    userId: UserId
  )

  import zio.ZIO

  def registrationOptions()
    : ZIO[WebAuthnService, WebAuthnError, BeginRegResult] =
    ZIO.serviceWithZIO[WebAuthnService](_.registrationOptions())

  def registrationVerify(tx: AuthTx, json: String): ZIO[WebAuthnService, WebAuthnError, RegistrationVerified] =
    ZIO.serviceWithZIO[WebAuthnService](_.registrationVerify(tx, json))

  //  def finishRegistration(cmd: FinishRegCmd): ZIO[WebAuthnService, WebAuthnError, Unit] =
//    ZIO.serviceWithZIO[WebAuthnService](_.finishRegistration(cmd))
//
//  def beginAuthentication(cmd: BeginAuthCmd): ZIO[WebAuthnService, WebAuthnError, BeginAuthResult] =
//    ZIO.serviceWithZIO[WebAuthnService](_.beginAuthentication(cmd))
//
//  def finishAuthentication(cmd: FinishAuthCmd): ZIO[WebAuthnService, WebAuthnError, Unit] =
//    ZIO.serviceWithZIO[WebAuthnService](_.finishAuthentication(cmd))
}
