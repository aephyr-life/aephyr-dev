package aephyr.api.v0.identity

import aephyr.api.shared.{AuthenticationContext, Problem}
import aephyr.api.TapirDsl.{jsonOut, secureBaseBearer}
import dto.{Me, MeError}
import sttp.tapir.*

object IdentityApi {

  val getMe: Endpoint[AuthenticationContext, Unit, Problem[MeError], Me, Any] =
    secureBaseBearer[MeError]
      .get
      .in("me")
      .out(jsonOut[Me])
}
