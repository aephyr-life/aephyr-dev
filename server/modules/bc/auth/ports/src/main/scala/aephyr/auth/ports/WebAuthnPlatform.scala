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
    requestJson: String,
    responseJson: String
  ): IO[Throwable, FinishedRegistration]

  def startAssertion(): IO[Throwable, StartAssertionResult]

  def finishAssertion(
                     requestJson: String, 
                     responseJson: String 
                   ): IO[Throwable, FinishedAssertion]
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
