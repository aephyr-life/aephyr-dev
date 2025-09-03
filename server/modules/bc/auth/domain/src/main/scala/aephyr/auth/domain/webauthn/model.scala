package aephyr.auth.domain.webauthn

import aephyr.kernel.types.Bytes

import aephyr.auth.domain.UserHandle
import scala.concurrent.duration.FiniteDuration

// --- Basic value types ---
opaque type RelyingPartyId = String
object RelyingPartyId {
  def apply(v: String): RelyingPartyId = v
  extension (r: RelyingPartyId) {
    def value: String = r
  }
}

final case class Challenge(bytes: Bytes) derives CanEqual
final case class CredentialId(bytes: Bytes) derives CanEqual
final case class PublicKeyCose(bytes: Bytes) derives CanEqual

// --- RP & User ---
final case class RpEntity(
  id: RelyingPartyId,
  name: String,
  icon: Option[String] = None
) derives CanEqual

final case class UserEntity(
  handle: UserHandle,
  name: String, // username (e.g. "j.doe")
  displayName: Option[String] // human friendly
) derives CanEqual

// --- Enums / ADTs mirroring WebAuthn choices ---
enum UserVerification derives CanEqual {
  case  Required, Preferred, Discouraged
}

enum ResidentKey derives CanEqual {
  case Required, Preferred, Discouraged
}

enum AuthenticatorAttachment derives CanEqual {
  case Platform, CrossPlatform
}

final case class AuthenticatorSelection(
  userVerification: Option[UserVerification] = None,
  residentKey: Option[ResidentKey] = None,
  attachment: Option[AuthenticatorAttachment] = None
) derives CanEqual

enum AttestationConveyance derives CanEqual {
  case Indirect, Direct, Enterprise
}

// COSE alg id (e.g. -7 = ES256, -257 = RS256)
final case class PubKeyCredParam(alg: Int)

// Transports for descriptors
enum AuthenticatorTransport {
  case Usb, Nfc, Ble, Internal, Hybrid
}

// PublicKeyCredentialDescriptor (subset)
final case class CredDescriptor(
  id: CredentialId,
  transports: Set[AuthenticatorTransport] = Set.empty
)

// --- Options returned to the client (creation / request) ---
final case class CreationOptions(
  rp: RpEntity,
  user: UserEntity,
  challenge: Challenge,
  pubKeyCredParams: List[PubKeyCredParam],
  timeout: Option[FiniteDuration] = None,
  excludeCredentials: List[CredDescriptor] = Nil,
  authenticatorSelection: Option[AuthenticatorSelection] = None,
  attestation: Option[AttestationConveyance] = None
)

final case class RequestOptions(
  challenge: Challenge,
  timeout: Option[FiniteDuration] = None,
  allowCredentials: List[CredDescriptor] = Nil,
  userVerification: Option[UserVerification] = None
)

// --- Client responses (already base64url-decoded to raw bytes) ---
final case class RegistrationResponseData(
  clientDataJSON: Bytes,
  attestationObject: Bytes
)

final case class RegistrationResponse(
  id: CredentialId,
  rawId: CredentialId,
  response: RegistrationResponseData
)

final case class AssertionResponseData(
  clientDataJSON: Bytes,
  authenticatorData: Bytes,
  signature: Bytes,
  userHandle: Option[Bytes]
)

final case class AssertionResponse(
  id: CredentialId,
  rawId: CredentialId,
  response: AssertionResponseData
)
