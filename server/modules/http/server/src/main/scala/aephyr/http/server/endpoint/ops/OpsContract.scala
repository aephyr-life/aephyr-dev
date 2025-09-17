package aephyr.http.server.endpoint.ops

import sttp.tapir.*

object OpsContract {
  
  val health: PublicEndpoint[Unit, Unit, String, Any] =
    endpoint.get
      .in("api" / "health")
      .out(stringBody)
}
