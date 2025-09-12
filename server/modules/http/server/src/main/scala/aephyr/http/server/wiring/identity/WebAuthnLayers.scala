package aephyr.http.server.wiring.identity

import aephyr.adapters.security.jwt.NimbusJwtIssuer
import aephyr.shared.config.AppConfig
import aephyr.adapters.security.webauthn.memory.{InMemoryChallengeStore, InMemoryRelyingParty, InMemoryUserHandleRepo, InMemoryWebAuthnRepo}
import aephyr.adapters.security.webauthn.yubico.WebAuthnPlatformYubico
import aephyr.auth.application.webauthn.{WebAuthnService, WebAuthnServiceLive}
import aephyr.auth.ports.JwtIssuer
import aephyr.http.server.endpoint.HttpTypes
import zio.*

object WebAuthnLayers {
  type In  = AppConfig & Clock
  type Env = HttpTypes.WebAuthnEnv

  // Choose impls per profile; start with in-memory for dev
  val dev: ZLayer[In, Throwable, HttpTypes.WebAuthnEnv] =
    ZLayer.makeSome[In, HttpTypes.WebAuthnEnv](
      InMemoryUserHandleRepo.live,
      InMemoryWebAuthnRepo.live,
      InMemoryChallengeStore.live(),
      InMemoryRelyingParty.live,   // uses AppConfig.auth.webauthn (rpId, origins, etc.)
      NimbusJwtIssuer.layer,
      WebAuthnPlatformYubico.layer,
      WebAuthnServiceLive.layer    // depends on repos + platform + RP + challenge store
    )

  // val prod: ZLayer[Any, Throwable, Env] = ...
}
