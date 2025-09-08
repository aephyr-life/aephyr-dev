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
  val documented: List[ZSE[DocumentedEnv]] =
    OpsHandler.serverEndpoints

  /**
   * Endpoints you don’t want in docs (static, well-known, etc.).
   */
  val undocumented: List[ZSE[UndocumentedEnv]] =
    StaticHandler.serverEndpoints.map(_.widen[UndocumentedEnv]) ++
    WellKnownHandler.serverEndpoints.map(_.widen[UndocumentedEnv])

  /**
   * All public endpoints that require no environment.
   */
  val public: List[ZSE[PublicEnv]] =
    documented.map(_.widen[PublicEnv]) ++
    undocumented.map(_.widen[PublicEnv])

  /**
   * Identity / protected endpoints (need IdentityEnv).
   */
  val identity: List[ZSE[Env]] =
    MeHandler.serverEndpoints.map(_.widen[Env]) ++
      WebAuthnHandler.serverEndpoints.map(_.widen[Env])
}
