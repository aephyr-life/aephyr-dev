package aephyr.http.server.wiring.identity

import aephyr.shared.config.AppConfig
import aephyr.adapters.security.webauthn.memory.{InMemoryChallengeStore, InMemoryRelyingParty, InMemoryUserHandleRepo, InMemoryWebAuthnRepo}
import aephyr.adapters.security.webauthn.yubico.WebAuthnPlatformYubico
import aephyr.auth.application.webauthn.{WebAuthnService, WebAuthnServiceLive}
import zio.*

object WebAuthnLayers {
  type In  = AppConfig & Clock
  type Env = WebAuthnService

  // Choose impls per profile; start with in-memory for dev
  val dev: ZLayer[In, Throwable, Env] =
    ZLayer.makeSome[In, Env](
      InMemoryUserHandleRepo.live,
      InMemoryWebAuthnRepo.live,
      InMemoryChallengeStore.live(),
      InMemoryRelyingParty.live,   // uses AppConfig.auth.webauthn (rpId, origins, etc.)
      WebAuthnPlatformYubico.layer,
      WebAuthnServiceLive.layer    // depends on repos + platform + RP + challenge store
    )

  // val prod: ZLayer[Any, Throwable, Env] = ...
}
