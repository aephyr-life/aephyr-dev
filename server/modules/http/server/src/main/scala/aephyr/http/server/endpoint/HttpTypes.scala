package aephyr.http.server.endpoint

import aephyr.http.server.endpoint.identity.{MeHandler, WebAuthnHandler}
import aephyr.http.server.endpoint.ops.OpsHandler
import aephyr.http.server.endpoint.www.WellKnownHandler
import aephyr.http.server.app.identity.MeService
import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.shared.config.AasaCfg

object HttpTypes {

  type Caps = ZioStreams & WebSockets

  type ZSE[R] = ZServerEndpoint[R, Caps]

  type MeEnv = MeService
  type WebAuthnEnv = WebAuthnService
  type OpsHandlerEnv = Any
  type WellKnownEnv = AasaCfg

  type DocumentedEnv = OpsHandlerEnv
  type UndocumentedEnv = WellKnownEnv
  type PublicEnv = DocumentedEnv & UndocumentedEnv
  type IdentityEnv = MeEnv & WebAuthnEnv
  type Env = PublicEnv & IdentityEnv
}
