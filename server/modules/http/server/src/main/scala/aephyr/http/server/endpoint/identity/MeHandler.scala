package aephyr.http.server.endpoint.identity

import aephyr.api.v0.identity.dto.MeError
import aephyr.api.shared.Problem
import aephyr.api.shared.AuthenticationContext
import aephyr.api.v0.identity.IdentityApi
import aephyr.http.server.app.identity.MeService
import aephyr.http.server.endpoint.HttpTypes.*

import sttp.tapir.ztapir.*
import zio.*

object MeHandler {

  val me: ZSE[MeService] =
    IdentityApi.getMe
      .zServerSecurityLogic { (authCtx: AuthenticationContext) =>
        ZIO.succeed(authCtx) // oder: ZIO.service[AuthService].flatMap(_.authenticate(authCtx))
      }
      .serverLogic { (authCtx: AuthenticationContext) => (_: Unit) =>
        ZIO.service[MeService]
          .flatMap(_.me(/* authCtx.bearer */ ???))
          .mapError { e =>
            Problem[MeError](
              `type` = "about:blank",
              title = "Me error",
              status = 400,
              detail = Some(e.toString),
              traceId = None,
              instance = None,
              error = Some(e)
            )
          }
    }

  val serverEndpoints: List[ZSE[MeEnv]] =
    List(me)
}
