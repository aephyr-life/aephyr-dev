package aephyr.web.server.routes.api

import zio.*
import zio.http.*
import sttp.tapir.AnyEndpoint
import sttp.tapir.docs.apispec.redoc.RedocInterpreter
import sttp.tapir.redoc.RedocUIOptions
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ziohttp.ZioHttpServerInterpreter

object RedocRoutes {

  /** Build zio-http Routes for ReDoc docs from plain Tapir endpoints. */
  def fromEndpoints(
                     endpoints: List[AnyEndpoint],
                     title: String = "Aephyr API",
                     version: String = "v0",
                     pathPrefix: List[String] = List("api")        // -> /api/docs by default
                   ): Routes[Any, Response] = {
    val redocServerEndpoints: List[ServerEndpoint[Any, Task]] =
      RedocInterpreter(
        redocUIOptions = RedocUIOptions.default.copy(pathPrefix = pathPrefix)
      ).fromEndpoints[Task](endpoints, title, version)

    // Convert Tapir server endpoints -> zio-http routes
    ZioHttpServerInterpreter[Task]().toRoutes(redocServerEndpoints)
  }

  /** If you have ZServerEndpoints, map them to plain endpoints and reuse above. */
  def fromServerEndpoints(
                           serverEndpoints: List[sttp.tapir.ztapir.ZServerEndpoint[?, ?]],
                           title: String = "Aephyr API",
                           version: String = "v0",
                           pathPrefix: List[String] = List("api")
                         ): Routes[Any, Response] =
    fromEndpoints(serverEndpoints.map(_.endpoint), title, version, pathPrefix)

  /** Helper to widen Any-env routes to your app env when combining. */
  def widenEnv[R](r: Routes[Any, Response]): Routes[R, Response] =
    r.asInstanceOf[Routes[R, Response]] // safe: needs no env
}
