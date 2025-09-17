package aephyr.adapters.security.webauthn.yubico

import aephyr.adapters.security.webauthn.yubico.Interop.*
import aephyr.auth.domain.webauthn.*
import aephyr.auth.ports.WebAuthnPlatform
import aephyr.auth.ports.WebAuthnPlatform.FinishedRegistration
import aephyr.kernel.types.Bytes
import com.yubico.internal.util.JacksonCodecs
import com.yubico.webauthn.data.{AuthenticatorSelectionCriteria, PublicKeyCredential, PublicKeyCredentialCreationOptions, ResidentKeyRequirement, UserVerificationRequirement}

import scala.jdk.CollectionConverters.*
import scala.language.unsafeNulls
import com.yubico.webauthn.*
import zio.*

final class WebAuthnPlatformYubico(rp: RelyingParty) extends WebAuthnPlatform:
  // ----- Registration -----

  override def startRegistration(user: UserEntity): IO[Throwable, StartRegistrationResult] =
    ZIO.attempt {
      val sel = AuthenticatorSelectionCriteria.builder()
        .userVerification(UserVerificationRequirement.REQUIRED)
        .residentKey(ResidentKeyRequirement.PREFERRED)
        .build()

      val sro = StartRegistrationOptions.builder()
        .user(user.toYUser)
        .authenticatorSelection(sel)
        .build()

      val opts = rp.startRegistration(sro)
      StartRegistrationResult(
        clientJson = opts.toCredentialsCreateJson,
        serverJson = opts.toJson
      )
    }

  private inline def opt[A](o: A | Null): Option[A] = Option(o).map(_.nn)

  private def uuidFrom16(bytes: Array[Byte]): Option[java.util.UUID] =
    if (bytes != null && bytes.length == 16) {
      val bb = java.nio.ByteBuffer.wrap(bytes).asLongBuffer()
      Some(new java.util.UUID(bb.get(0), bb.get(1)))
    } else None

  override def finishRegistration(
                                   requestJson: String,
                                   responseJson: String
                                 ): IO[Throwable, FinishedRegistration] =
    ZIO.attempt {
      val creation  = PublicKeyCredentialCreationOptions.fromJson(requestJson)
      val response  = PublicKeyCredential.parseRegistrationResponseJson(responseJson)

      val res = rp.finishRegistration(
        com.yubico.webauthn.FinishRegistrationOptions.builder()
          .request(creation)
          .response(response)
          .build()
      )

      val aaguidUuid: Option[java.util.UUID] =
        opt(res.getAaguid).flatMap(ba => uuidFrom16(ba.getBytes))

      // Transports Optional -> List[String]
      val transports: List[String] = {
        val keyId = res.getKeyId
        if (keyId != null && keyId.getTransports != null && keyId.getTransports.isPresent)
          keyId.getTransports.get.asScala.map(_.toString).toList
        else Nil
      }

      FinishedRegistration(
        credentialId  = Bytes.fromArray(res.getKeyId.getId.getBytes),
        publicKeyCose = Bytes.fromArray(res.getPublicKeyCose.getBytes),
        signCount     = res.getSignatureCount.toLong,
        aaguid        = aaguidUuid,
        transports    = transports
      )
    }

  override def startAssertion(): IO[Throwable, StartAssertionResult] =
    ZIO.attempt {
      val sao = StartAssertionOptions.builder()
        .userVerification(UserVerificationRequirement.REQUIRED)
        .build()

      // val tx = AuthTx.generate()
      // pending.storeAssertion(tx, req.toJson)

      val req = rp.startAssertion(sao)

      val clientJson =
        req.getPublicKeyCredentialRequestOptions.toCredentialsGetJson

      val serverJson =
        JacksonCodecs.json().writeValueAsString(req)

      StartAssertionResult(clientJson, serverJson)
    }

  def finishAssertion(serverJson: String, responseJson: String): IO[Throwable, FinishedAssertion] =
    ZIO.attempt {
      val request = JacksonCodecs.json().readValue(serverJson, classOf[AssertionRequest])
      val response = PublicKeyCredential.parseAssertionResponseJson(responseJson)

      val res = rp.finishAssertion(
        FinishAssertionOptions.builder()
          .request(request)
          .response(response)
          .build()
      )

      if (!res.isSuccess) throw new RuntimeException("WebAuthn assertion verification failed")

      FinishedAssertion(
        credentialId  = Bytes.fromArray(res.getCredential.getCredentialId.getBytes),
        userHandle    = Option(Bytes.fromArray(res.getCredential.getUserHandle.getBytes)),
        signCount     = res.getSignatureCount,
        userVerified  = res.isUserVerified
      )
    }

object WebAuthnPlatformYubico:
  val layer: ZLayer[RelyingParty, Nothing, WebAuthnPlatform] =
    ZLayer.fromFunction(WebAuthnPlatformYubico.apply)
