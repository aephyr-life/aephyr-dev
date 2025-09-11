package aephyr.auth.ports

import zio.IO
import aephyr.auth.domain.webauthn.*

trait WebAuthnPlatform {

  def startRegistration(user: UserEntity): IO[Throwable, StartRegistrationResult]

//  def finishRegistration(
//                          requestJson: String,
//                          response: RegistrationResponse
//                        ): IO[Throwable, (CredentialId, PublicKeyCose, Long, UserHandle)]
//
//  def startAssertion(
//                      userVerification: Option[UserVerification],
//                      allow: List[CredDescriptor]
//                    ): IO[Throwable, (RequestOptions, String)]
//
//  def finishAssertion(
//                       requestJson: String,
//                       response: AssertionResponse
//                     ): IO[Throwable, (CredentialId, Long)]
}
