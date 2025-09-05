package aephyr.http.server.endpoint.ops

import sttp.tapir.ztapir.*
import zio.*

object OpsHandler {
  
  type Env = Any
  
  val health: ZServerEndpoint[Any, Any] =
    OpsContract.health
      .zServerLogic(_ => ZIO.succeed("ok"))
  
  val serverEndpoints: List[ZServerEndpoint[Env, Any]] = 
    List(health)
}
