//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes.api
import aephyr.identity.application.MagicLinkService
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir._
import zio._
import zio.http._

object ApiRoutes:

  type Env = MagicLinkService

  private val apiEndpoints: List[ZServerEndpoint[MagicLinkService, Any]] =
    IdentityApiRoutes.all

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
    apiRoutes ++ docsRoutes
