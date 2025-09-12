package aephyr.http.server.endpoint

import aephyr.http.server.app.identity.MeService
import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.auth.ports.JwtIssuer
import aephyr.http.server.security.AuthService
import aephyr.shared.config.AasaCfg
import sttp.capabilities.zio.ZioStreams
import sttp.capabilities.WebSockets
import sttp.tapir.ztapir.ZServerEndpoint

object HttpTypes {
  type Caps = ZioStreams & WebSockets
  type ZSE[R] = ZServerEndpoint[R, Caps]

  // Feature/env atoms
  type MeEnv           = MeService
  type WebAuthnEnv     = WebAuthnService & JwtIssuer
  type SecurityEnv     = AuthService
  type OpsHandlerEnv   = Any
  type WellKnownEnv    = AasaCfg

  // Groups
  type DocumentedEnv   = OpsHandlerEnv
  type UndocumentedEnv = WellKnownEnv
  type PublicEnv       = DocumentedEnv & UndocumentedEnv
  type IdentityEnv     = MeEnv & WebAuthnEnv

  // Final server environment (now includes auth)
  type Env             = PublicEnv & IdentityEnv & SecurityEnv
}
