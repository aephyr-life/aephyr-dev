package aephyr.http.server.endpoint.www

import dto.Aasa
import dto.AasaWebCredentials
import aephyr.shared.config.AasaCfg
import aephyr.http.server.endpoint.HttpTypes.*

import sttp.tapir.ztapir.*
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import zio.*

object WellKnownHandler {

  val aasa: ZServerEndpoint[AasaCfg, Caps] =
    WellKnownContract.aasa.zServerLogic { _ =>
      for {
        cfg <- ZIO.service[AasaCfg]
        apps = List(s"${cfg.teamId}.${cfg.bundleId}")
        aasa = Aasa(AasaWebCredentials(apps))
      } yield aasa
    }

  val serverEndpoints: List[ZServerEndpoint[WellKnownEnv, Caps]] =
    List(aasa)
}
