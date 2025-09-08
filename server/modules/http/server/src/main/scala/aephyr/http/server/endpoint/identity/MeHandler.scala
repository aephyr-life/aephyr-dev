package aephyr.http.server.endpoint.identity

import aephyr.api.v0.identity.dto.MeError
import aephyr.api.shared.{AuthenticationContext, Problem}
import aephyr.api.v0.identity.IdentityApi
import aephyr.http.server.app.identity.MeService
import aephyr.http.server.endpoint.HttpTypes.*
import aephyr.http.server.security.{AuthErrorToProblem, AuthService, SecurityDsl}
import sttp.tapir.ztapir.*
import zio.*

object MeHandler {

  private type MeSecuredEnv = MeEnv & AuthService
  
  // map auth errors to Problem[MeError] (simple default)
  given AuthErrorToProblem[MeError] =
    AuthErrorToProblem.default[MeError]("Authentication failed")

  val me: ZSE[MeSecuredEnv] =
    IdentityApi.getMe
      .zServerSecurityLogic(SecurityDsl.secureBase[MeError].securityLogic)
      .serverLogic { (authCtx: AuthenticationContext) => (_: Unit) =>
        ZIO.serviceWithZIO[MeService](_.me(authCtx)) // or bearer.value if that’s the signature
          .mapError { e =>
            Problem[MeError](
              `type`   = "about:blank",
              title    = "Me error",
              status   = 400,
              detail   = Some(e.toString),
              traceId  = None,
              instance = None,
              error    = Some(e)
            )
          }
      }

  val serverEndpoints: List[ZSE[MeSecuredEnv]] = List(me)
}
