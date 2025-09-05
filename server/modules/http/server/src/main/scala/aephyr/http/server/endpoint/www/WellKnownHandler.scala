package aephyr.http.server.endpoint.www

import aephyr.shared.config.AasaCfg

import sttp.tapir.ztapir.*

object WellKnownHandler {

  type Env = AasaCfg
  
  val serverEndpoints: List[ZServerEndpoint[Env, Any]] = 
    List(aasea)
  
  val aasa: ZServerEndpoint[Env, Any] =
    WellKnownContract.aasa.zServerLogic { _ =>
      for {
        cfg <- ZIO.service[AasaCfg]
        apps = List(s"${cfg.teamId}.${cfg.bundleId}")
        aasa = Aasa(AasaWebCredentials(apps))
      } yield aasa
    }
}
