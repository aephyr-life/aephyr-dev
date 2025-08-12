package aephyr.web.server.routes.api

import scala.language.unsafeNulls

import sttp.tapir.AnyEndpoint
import sttp.tapir.redoc.RedocUIOptions
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio._
import zio.http._

object RedocRoutes {

  /** Build zio-http Routes for ReDoc docs from plain Tapir endpoints. */
  def fromEndpoints(
    endpoints: List[AnyEndpoint],
    title: String = "Aephyr API",
    version: String = "v0",
    pathPrefix: List[String] = List("api") // -> /api/docs by default
  ): Routes[Any, Response] = {
    val redocServerEndpoints: List[ServerEndpoint[Any, Task]] =
      RedocInterpreter(
        redocUIOptions = RedocUIOptions.default.copy(pathPrefix = pathPrefix)
      ).fromEndpoints[Task](endpoints, title, version)

    // Convert Tapir server endpoints -> zio-http routes
    ZioHttpInterpreter().toHttp(redocServerEndpoints)
  }

  /** If you have ZServerEndpoints, map them to plain endpoints and reuse above.
    */
  def fromServerEndpoints(
    serverEndpoints: List[ServerEndpoint[?, ?]],
    title: String = "Aephyr API",
    version: String = "v0",
    pathPrefix: List[String] = List("api")
  ): Routes[Any, Response] =
    fromEndpoints(serverEndpoints.map(_.endpoint), title, version, pathPrefix)

  /** Helper to widen Any-env routes to your app env when combining. */
  def widenEnv[R](r: Routes[Any, Response]): Routes[R, Response] =
    r.asInstanceOf[Routes[R, Response]] // safe: needs no env
}
