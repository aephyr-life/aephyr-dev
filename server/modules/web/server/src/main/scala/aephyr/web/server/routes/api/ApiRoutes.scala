//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes.api
import aephyr.auth.ports.{ChallengeStore, UserHandleRepo, WebAuthnRepo}
import com.yubico.webauthn.RelyingParty
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir.*
import zio.*
import zio.http.*

object ApiRoutes:

  type Env = RelyingParty & ChallengeStore & UserHandleRepo & WebAuthnRepo

  private val apiEndpoints: List[ZServerEndpoint[Env, Any]] =
    IdentityApiEndpoints.endpoints

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
    apiRoutes ++ docsRoutes ++ WebAuthnTestRoutes.routes
