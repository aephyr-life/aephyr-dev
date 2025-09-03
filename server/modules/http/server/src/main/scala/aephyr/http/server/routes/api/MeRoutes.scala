package aephyr.http.server.routes.api

import aephyr.http.apis.endpoints.v0.me.MeApi
import aephyr.http.apis.endpoints.v0.me.MeError
import aephyr.http.server.auth.AuthExtractor
import aephyr.http.server.service.me.MeService
import aephyr.identity.application.ports.UserReadPort
import aephyr.shared.config.AasaCfg
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir.ZServerEndpoint
import zio.ZIO
import sttp.tapir.ztapir.RichZEndpoint
import aephyr.http.apis.Problem

object MeRoutes {
  val routes: ZServerEndpoint[MeService, ZioStreams & WebSockets] =
    MeApi.me.zServerLogic { _ =>
      ZIO.serviceWithZIO[MeService](_.me)
        .mapError(Problem(_))
//        .mapError {
//          case MeError.NotAuthenticated => MeEndpoint.Errors.NotAuthenticated
//          case MeError.UserNotFound => MeEndpoint.Errors.NotFound
//        }
    }
}
