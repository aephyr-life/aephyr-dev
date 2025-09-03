package aephyr.auth.application.webauthn

import aephyr.auth.domain.webauthn.*

import aephyr.kernel.id.UserId

trait WebAuthnService {
  import zio.IO
  import WebAuthnService.*

  def beginRegistration(cmd: BeginRegCmd): IO[WebAuthnError, BeginRegResult]
  def finishRegistration(cmd: FinishRegCmd): IO[WebAuthnError, Unit]

  def beginAuthentication(cmd: BeginAuthCmd): IO[WebAuthnError, BeginAuthResult]
  def finishAuthentication(cmd: FinishAuthCmd): IO[WebAuthnError, Unit]
}

object WebAuthnService {

  // ---------- Commands ----------
  final case class BeginRegCmd(
    userId: UserId,
    username: String,
    displayName: Option[String],
    authenticatorSelection: Option[AuthenticatorSelection] = None,
    attestation: Option[AttestationConveyance] = None,
    excludeCredentials: List[CredDescriptor] = Nil
  )

  final case class FinishRegCmd(
    tx: String,
    response: RegistrationResponse,
    label: Option[String]
  )

  final case class BeginAuthCmd(
    username: Option[String] = None,
    allowCredentials: List[CredDescriptor] = Nil,
    userVerification: Option[UserVerification] = Some(UserVerification.Required)
  )

  final case class FinishAuthCmd(
    tx: String,
    response: AssertionResponse
  )

  // ---------- Results ----------
  final case class BeginRegResult(
    tx: String,
    options: CreationOptions
  )

  final case class BeginAuthResult(
    tx: String,
    options: RequestOptions
  )

  import zio.ZIO

  def beginRegistration(cmd: BeginRegCmd): ZIO[WebAuthnService, WebAuthnError, BeginRegResult] =
    ZIO.serviceWithZIO[WebAuthnService](_.beginRegistration(cmd))

  def finishRegistration(cmd: FinishRegCmd): ZIO[WebAuthnService, WebAuthnError, Unit] =
    ZIO.serviceWithZIO[WebAuthnService](_.finishRegistration(cmd))

  def beginAuthentication(cmd: BeginAuthCmd): ZIO[WebAuthnService, WebAuthnError, BeginAuthResult] =
    ZIO.serviceWithZIO[WebAuthnService](_.beginAuthentication(cmd))

  def finishAuthentication(cmd: FinishAuthCmd): ZIO[WebAuthnService, WebAuthnError, Unit] =
    ZIO.serviceWithZIO[WebAuthnService](_.finishAuthentication(cmd))
}
