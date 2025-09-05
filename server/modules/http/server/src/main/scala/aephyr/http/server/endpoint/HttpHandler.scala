package aephyr.http.server.endpoint

import identity.MeHandler
import identity.WebAuthnHandler
import ops.OpsHandler
import www.StaticHandler
import www.WellKnownHandler

import sttp.tapir.ztapir.*
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams

object HttpHandler {
  
  import HttpTypes.*

  // ---- Collect endpoints ----------------------------------------------------

  /**
   * Endpoints that you also want to include in docs (OpenAPI/ReDoc).
   */
  val documented: List[ZServerEndpoint[DocumentedEnv, HttpCaps]] =
    OpsHandler.serverEndpoints

  /**
   * Endpoints you don’t want in docs (static, well-known, etc.).
   */
  val undocumented: List[ZServerEndpoint[UndocumentedEnv, HttpCaps]] =
    StaticHandler.serverEndpoints ++
      WellKnownHandler.serverEndpoints

  /**
   * All public endpoints that require no environment.
   */
  val public: List[ZServerEndpoint[PublicEnv, HttpCaps]] =
    documented ++
      undocumented.asInstanceOf[List[ZServerEndpoint[PublicEnv, HttpCaps]]]


  /**
   * Identity / protected endpoints (need IdentityEnv).
   */
  val identity: List[ZServerEndpoint[IdentityEnv, HttpCaps]] =
    MeHandler.serverEndpoints ++ WebAuthnHandler.serverEndpoints
}
