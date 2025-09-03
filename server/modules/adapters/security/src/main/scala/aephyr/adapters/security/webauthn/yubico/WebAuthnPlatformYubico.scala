package aephyr.adapters.security.webauthn.yubico

//import aephyr.adapters.security.webauthn.yubico.DomainConversions
import aephyr.auth.domain.UserHandle
import aephyr.auth.domain.webauthn.*
import aephyr.auth.ports.WebAuthnPlatform

import scala.language.unsafeNulls
//import aephyr.auth.domain.webauthn.AttestationConveyance
//import aephyr.auth.domain.webauthn.AttestationConveyance.*
//import aephyr.auth.domain.webauthn.UserVerification
//import aephyr.auth.domain.webauthn.ResidentKey
import com.yubico.webauthn.*
//import com.yubico.webauthn.data.*
import zio.*

final class WebAuthnPlatformYubico(rp: RelyingParty) extends WebAuthnPlatform:

  // ----- Registration -----

  override def startRegistration(
                                  user: UserEntity,
                                  selection: Option[AuthenticatorSelection],
                                  attestation: Option[AttestationConveyance],
                                  exclude: List[CredDescriptor]
                                ): IO[Throwable, (CreationOptions, String)] =
    ZIO.fail(???)
//    ZIO.attempt {
//      val optsB = StartRegistrationOptions
//        .builder()
//        .user(toYubicoUser(user))
//        .attestationConveyancePreference(domAttestation(attestation))
//
//      selection.foreach(s => optsB.authenticatorSelection(toYubicoSelection(s)))
//      if exclude.nonEmpty then optsB.excludeCredentials(toYubicoExclude(exclude))
//
//      val req = rp.startRegistration(optsB.build())
//      val requestJson = req.toJson                          // server-side request JSON (store by tx)
//      val clientJson  = unwrapPublicKey(req.toCredentialsCreateJson) // inner publicKey JSON
//      (CreationOptions(clientJson), requestJson)
//    }

  override def finishRegistration(
                                   requestJson: String,
                                   response: RegistrationResponse
                                 ): IO[Throwable, (CredentialId, PublicKeyCose, Long, UserHandle)] =
    ZIO.fail(???)
//    ZIO.attempt {
//      val request = PublicKeyCredentialCreationOptions.fromJson(requestJson)
//
//      // Assuming your RegistrationResponse wraps the raw JSON produced by the browser
//      // e.g., case class RegistrationResponse(json: String)
//      val cred = PublicKeyCredential.parseRegistrationResponseJson(response.json)
//
//      val result = rp.finishRegistration(
//        FinishRegistrationOptions.builder()
//          .request(request)
//          .response(cred)
//          .build()
//      )
//
//      val credentialId = result.getKeyId.getId.getBytes
//      val publicKeyCose = result.getPublicKeyCose.getBytes
//      val userHandleBytes = result.getKeyId.getUser.getId.getBytes // falls back to request user id
//      val signCount = 0L // initial; some authenticators include count in attestation but 0 is fine
//
//      (CredentialId(credentialId), PublicKeyCose(publicKeyCose), signCount, UserHandle(userHandleBytes))
//    }

  // ----- Assertion -----

  override def startAssertion(
                               userVerification: Option[UserVerification],
                               allow: List[CredDescriptor]
                             ): IO[Throwable, (RequestOptions, String)] =
    ZIO.fail(???)
//    ZIO.attempt {
//      val b = StartAssertionOptions.builder()
//      userVerification.foreach(uv => b.userVerification(domUserVerification(uv)))
//      if allow.nonEmpty then
//        val allowList = toYubicoExclude(allow) // same shape as excludeCredentials
//        b.allowCredentials(allowList)
//
//      val req = rp.startAssertion(b.build())
//      val requestJson = req.toJson
//      val clientJson  = unwrapPublicKey(req.toCredentialsGetJson) // inner publicKey JSON
//      (RequestOptions(clientJson), requestJson)
//    }

  override def finishAssertion(
                                requestJson: String,
                                response: AssertionResponse
                              ): IO[Throwable, (CredentialId, Long)] =
    ZIO.fail(???)
//    ZIO.attempt {
//      val request = AssertionRequest.fromJson(requestJson)
//
//      // Assuming your AssertionResponse wraps the raw JSON string from the browser
//      val cred = PublicKeyCredential.parseAssertionResponseJson(response.json)
//
//      val result = rp.finishAssertion(
//        FinishAssertionOptions
//          .builder()
//          .request(request)
//          .response(cred)
//          .build()
//      )
//
//      val idBytes   = result.getCredential.getCredentialId.getBytes
//      val signCount = result.getCredential.getSignatureCount.toLong
//      (CredentialId(idBytes), signCount)
//    }

object WebAuthnPlatformYubico:
  val layer: ZLayer[RelyingParty, Nothing, WebAuthnPlatform] =
    ZLayer.fromFunction(WebAuthnPlatformYubico.apply)
