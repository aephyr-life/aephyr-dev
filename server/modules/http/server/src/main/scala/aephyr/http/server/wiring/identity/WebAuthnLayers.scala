package aephyr.http.server.wiring.identity

import aephyr.adapters.security.webauthn.memory.{InMemoryChallengeStore, InMemoryRelyingParty, InMemoryUserHandleRepo, InMemoryWebAuthnRepo}
import aephyr.adapters.security.webauthn.yubico.WebAuthnPlatformYubico
import aephyr.auth.application.webauthn.{WebAuthnService, WebAuthnServiceLive}
import zio.*

object WebAuthnLayers {
  type Env = WebAuthnService

  // Choose impls per profile; start with in-memory for dev
  val dev: ZLayer[Any, Throwable, Env] =
    InMemoryRelyingParty.live ++
      InMemoryChallengeStore.live() ++
      InMemoryUserHandleRepo.live ++
      InMemoryWebAuthnRepo.live ++
      WebAuthnPlatformYubico.layer ++
      WebAuthnServiceLive.layer

  // val prod: ZLayer[Any, Throwable, Env] = ...
}
