package aephyr.auth.ports

import zio.IO
import aephyr.auth.domain.webauthn.*
import aephyr.auth.ports.WebAuthnPlatform.FinishedRegistration
import aephyr.kernel.types.Bytes

import java.util.UUID

trait WebAuthnPlatform {

  def startRegistration(
    user: UserEntity
  ): IO[Throwable, StartRegistrationResult]

  def finishRegistration(
    requestJson: String, // the exact creationOptions JSON you stored (aka serverJson)
    responseJson: String // the raw PublicKeyCredential JSON from the client
  ): IO[Throwable, FinishedRegistration]

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

object WebAuthnPlatform {
  final case class FinishedRegistration(
    credentialId: Bytes, // credentialId bytes
    publicKeyCose: Bytes, // COSE-encoded public key
    signCount: Long,
    aaguid: Option[UUID],
    transports: List[String]
  )
}
