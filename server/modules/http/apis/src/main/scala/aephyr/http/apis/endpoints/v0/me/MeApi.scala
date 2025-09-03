package aephyr.http.apis.endpoints.v0.me

import aephyr.http.apis.Problem
import aephyr.http.apis.TapirDsl.*
import sttp.tapir.*

object MeApi {

  val me: PublicEndpoint[Unit, Problem[MeError], Me, Any] =
    base[MeError]
      .get
      .in("me")
      .out(jsonOut[Me])
}
