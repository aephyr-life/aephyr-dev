  package aephyr.adapters.security.webauthn.yubico

  import aephyr.auth.domain.webauthn.*
  import aephyr.auth.ports.WebAuthnPlatform
  import aephyr.auth.ports.WebAuthnPlatform.FinishedRegistration
  import aephyr.kernel.types.Bytes
  import com.yubico.webauthn.data.{AuthenticatorSelectionCriteria, PublicKeyCredentialCreationOptions, ResidentKeyRequirement, UserVerificationRequirement}

  import scala.jdk.CollectionConverters.*
  import com.yubico.webauthn.data.PublicKeyCredential

  import scala.language.unsafeNulls
  import com.yubico.webauthn.*
  import zio.*

  final class WebAuthnPlatformYubico(rp: RelyingParty) extends WebAuthnPlatform:

    import YubicoMapper.*
    // ----- Registration -----

    override def startRegistration(user: UserEntity): IO[Throwable, StartRegistrationResult] =
      ZIO.attempt {
        val sel = AuthenticatorSelectionCriteria.builder()
          .userVerification(UserVerificationRequirement.REQUIRED)
          .residentKey(ResidentKeyRequirement.PREFERRED)
          .build()

        val sro = StartRegistrationOptions.builder()
          .user(toYUser(user))
          .authenticatorSelection(sel)
          .build()

        val opts = rp.startRegistration(sro)
        StartRegistrationResult(
          clientJson = opts.toCredentialsCreateJson,
          serverJson = opts.toJson
        )
      }

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
          Option(res.getAaguid) // ByteArray | null
            .map(_.getBytes) // Array[Byte]
            .filter(bs => bs != null && bs.nonEmpty)
            // Pick ONE of the conversions below. nameUUIDFromBytes is simple and stable:
            .map(java.util.UUID.nameUUIDFromBytes)
            // If you eventually want the *real* AAGUID semantics (16 fixed bytes),
            // you can parse ByteBuffer to UUID (when you’re sure length == 16).

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

  object WebAuthnPlatformYubico:
    val layer: ZLayer[RelyingParty, Nothing, WebAuthnPlatform] =
      ZLayer.fromFunction(WebAuthnPlatformYubico.apply)
