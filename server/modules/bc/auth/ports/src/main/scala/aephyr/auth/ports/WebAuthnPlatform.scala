package aephyr.auth.ports

import aephyr.auth.domain.UserHandle
import zio.IO
import aephyr.auth.domain.webauthn.*

/** Thin facade over Yubico. Returns domain models for the app
 * plus the raw JSON that must be stored and later verified.
 */
trait WebAuthnPlatform {
  def startRegistration(
                         user: UserEntity,
                         selection: Option[AuthenticatorSelection],
                         attestation: Option[AttestationConveyance],
                         exclude: List[CredDescriptor]
                       ): IO[Throwable, (CreationOptions, String)]

  def finishRegistration(
                          requestJson: String,
                          response: RegistrationResponse
                        ): IO[Throwable, (CredentialId, PublicKeyCose, Long, UserHandle)]

  def startAssertion(
                      userVerification: Option[UserVerification],
                      allow: List[CredDescriptor]
                    ): IO[Throwable, (RequestOptions, String)]

  def finishAssertion(
                       requestJson: String,
                       response: AssertionResponse
                     ): IO[Throwable, (CredentialId, Long)]
}
