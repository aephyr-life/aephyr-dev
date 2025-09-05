package aephyr.http.server.endpoint.ops

import scala.language.unsafeNulls

import sttp.tapir.AnyEndpoint
import sttp.tapir.redoc.RedocUIOptions
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.*
import zio.http.*

object DocsHandler {

  def fromEndpoints(
    endpoints: List[AnyEndpoint]
  ): List[ServerEndpoint[Any, Task]] = {
    val title = "Aephyr API"
    val version = "v0"
    val pathPrefix: List[String] = List("api")
    RedocInterpreter(
      redocUIOptions = RedocUIOptions.default.copy(pathPrefix = pathPrefix)
    ).fromEndpoints[Task](endpoints, title, version)
  }
}
