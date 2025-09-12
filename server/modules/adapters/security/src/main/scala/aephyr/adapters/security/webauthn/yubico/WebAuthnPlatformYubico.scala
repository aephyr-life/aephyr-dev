package aephyr.adapters.security.webauthn.yubico


import aephyr.auth.domain.webauthn.*
import aephyr.auth.ports.WebAuthnPlatform
import com.yubico.webauthn.data.{AuthenticatorSelectionCriteria, ResidentKeyRequirement, UserVerificationRequirement}

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


object WebAuthnPlatformYubico:
  val layer: ZLayer[RelyingParty, Nothing, WebAuthnPlatform] =
    ZLayer.fromFunction(WebAuthnPlatformYubico.apply)
