package aephyr.http.server.endpoint.ops

import aephyr.http.server.endpoint.HttpTypes.*

import sttp.tapir.ztapir.*
import zio.*

object OpsHandler {
  
  val health: ZServerEndpoint[OpsHandlerEnv, Caps] =
    OpsContract.health
      .zServerLogic(_ => ZIO.succeed("ok"))
  
  val serverEndpoints: List[ZServerEndpoint[OpsHandlerEnv, Caps]] = 
    List(health)
}
