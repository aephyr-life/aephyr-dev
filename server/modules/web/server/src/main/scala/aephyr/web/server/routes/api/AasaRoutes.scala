package aephyr.web.server.routes.api

import aephyr.shared.config.AasaCfg

import sttp.tapir.ztapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import zio.*
import zio.json.*

final case class AasaWebCredentials(apps: List[String]) derives JsonCodec

final case class Aasa(webcredentials: AasaWebCredentials) derives JsonCodec

object AasaRoutes {

  private val eAasa = endpoint
    .get
    .in(".well-known" / "apple-app-site-association")
    .out(jsonBody[Aasa])

  val route =
    eAasa.zServerLogic { _ =>
      for {
        cfg <- ZIO.service[AasaCfg]
        apps = List(s"${cfg.teamId}.${cfg.bundleId}")
        aasa = Aasa(AasaWebCredentials(apps))
      } yield aasa
    }
}
