package aephyr.http.server.endpoint

import identity.MeHandler
import identity.WebAuthnHandler
import ops.OpsHandler
import www.StaticHandler
import www.WellKnownHandler

import sttp.tapir.ztapir.*

object HttpHandler {

  type DocumentedEnv = OpsHandler.Env
  type UndocumentedEnv = WellKnownHandler.Env
  type PublicEnv = DocumentedEnv & UndocumentedEnv
  type IdentityEnv = MeHandler.Env & WebAuthnHandler.Env
  type Env = PublicEnv & DocumentedEnv & UndocumentedEnv & IdentityEnv 

  // ---- Collect endpoints ----------------------------------------------------
  
  /** 
   * Endpoints that you also want to include in docs (OpenAPI/ReDoc). 
   */
  val documented: List[ZServerEndpoint[DocumentedEnv, Any]] =
    OpsHandler.serverEndpoints

  /** 
   * Endpoints you don’t want in docs (static, well-known, etc.). 
   */
  val undocumented: List[ZServerEndpoint[UndocumentedEnv, Any]] =
    StaticHandler.serverEndpoints ++
      WellKnownHandler.serverEndpoints

  /** 
   * All public endpoints that require no environment. 
   */
  val public: List[ZServerEndpoint[PublicEnv, Any]] =
    documented ++ undocumented

  /** 
   * Identity / protected endpoints (need IdentityEnv). 
   */
  val identity: List[ZServerEndpoint[IdentityEnv, Any]] =
    MeHandler.serverEndpoints ++ WebAuthnHandler.serverEndpoints
}
