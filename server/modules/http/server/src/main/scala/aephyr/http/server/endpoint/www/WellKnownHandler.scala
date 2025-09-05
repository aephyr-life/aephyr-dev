package aephyr.http.server.endpoint.www

import aephyr.shared.config.AasaCfg

import sttp.tapir.ztapir.*
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams

object WellKnownHandler {
  
  import HttpTypes.*
  
  val serverEndpoints: List[ZServerEndpoint[AasaEnv, Caps]] = 
    List(aasea)
  
  val aasa: ZServerEndpoint[AasaCfg, Caps] =
    WellKnownContract.aasa.zServerLogic { _ =>
      for {
        cfg <- ZIO.service[AasaCfg]
        apps = List(s"${cfg.teamId}.${cfg.bundleId}")
        aasa = Aasa(AasaWebCredentials(apps))
      } yield aasa
    }
}
