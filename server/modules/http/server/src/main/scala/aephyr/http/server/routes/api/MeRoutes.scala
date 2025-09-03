package aephyr.http.server.routes.api

import aephyr.http.server.auth.AuthExtractor
import aephyr.identity.application.ports.UserReadPort
import aephyr.shared.config.AasaCfg
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir.ZServerEndpoint

object MeRoutes {
  val routes: ZServerEndpoint[AuthExtractor & UserReadPort, ZioStreams & WebSockets] = ???
}
