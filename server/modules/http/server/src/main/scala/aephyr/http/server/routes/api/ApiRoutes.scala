package aephyr.http.server.routes.api

import aephyr.auth.application.webauthn.WebAuthnService
import aephyr.http.server.routes.api.WebAuthnRoutes
import aephyr.auth.ports.{ChallengeStore, UserHandleRepo, WebAuthnRepo}
import com.yubico.webauthn.RelyingParty
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir.*
import zio.*
import zio.http.*
import aephyr.http.server.routes.www.StaticRoutes

object ApiRoutes:

  type Env = WebAuthnService // RelyingParty & ChallengeStore & UserHandleRepo & WebAuthnRepo
  type Caps = Any
  
  private val eHealth = sttp.tapir.ztapir.endpoint.get.in("api" / "health").out(stringBody)

  val healthEp: ZServerEndpoint[Env, Caps] =
    eHealth
      .zServerLogic { _ => ZIO.succeed("ok") }
      .asInstanceOf[ZServerEndpoint[Env, Caps]]

  val aasaEp: ZServerEndpoint[Env, Caps] =
    AasaRoutes.route.asInstanceOf[ZServerEndpoint[Env, Caps]]

  private val apiEndpoints: List[ZServerEndpoint[Env, Caps]] =
    List(healthEp, aasaEp) ++ 
      StaticRoutes.endpoints.map(_.asInstanceOf[ZServerEndpoint[Env, Caps]])

  // Your business routes built from server endpoints
  private val apiRoutes: Routes[Env, Response] =
    ZioHttpInterpreter().toHttp(apiEndpoints)

  // Docs built from the *plain* endpoints
  private val docsRoutesAny: Routes[Any, Response] =
    RedocRoutes.fromEndpoints(
      apiEndpoints.map(_.endpoint),
      title = "Aephyr API",
      version = "v0",
      pathPrefix = List("api")
    )

  // Widen docs to your Env so you can combine
  private val docsRoutes: Routes[Env, Response] =
    RedocRoutes.widenEnv[Env](docsRoutesAny)


  // Final combined router
  val routes: Routes[Env, Response] =
    apiRoutes ++ docsRoutes ++ WebAuthnRoutes.routes
