package aephyr.http.server.endpoint.identity

import aephyr.api.v0.identity.IdentityApi
import aephyr.http.server.app.identity.MeService

import sttp.tapir.ztapir.*

object MeHandler {

  import HttpTypes.*

  val serverEndpoints: List[ZServerEndpoint[MeEnv, Any]] =
    List(me)

  val me: ZServerEndpoint[MeService, Any] =
    IdentityApi.me.zServerLogic { _ =>
      ZIO.serviceWithZIO[MeService](_.me)
        .mapError(Problem(_))
      //        .mapError {
      //          case MeError.NotAuthenticated => MeEndpoint.Errors.NotAuthenticated
      //          case MeError.UserNotFound => MeEndpoint.Errors.NotFound
      //        }
    }
}
