package aephyr.http.server.wiring

import zio.*
import aephyr.http.server.endpoint.HttpHandler
import aephyr.http.server.endpoint.HttpTypes
import infra.InfraLayers
import identity.MeLayers
import identity.WebAuthnLayers
import aephyr.shared.config.{DbCfg, AasaCfg, AppConfig}

object HttpAppLayers {

  /** Final environment required by HttpRoutes.routes. */
  type Env = HttpTypes.Env
  type In  = DbCfg & AasaCfg & AppConfig & Clock

  /** Profile-based assembly (dev here; add staging/prod later). */
  val dev: ZLayer[In, Throwable, Env] =
    InfraLayers.live ++
      MeLayers.dev ++
      WebAuthnLayers.dev ++
      ZLayer.service[AasaCfg]

  // val prod: ZLayer[Any, Throwable, Env] = InfraLayers.live ++ IdentityLayers.prod ++ WebAuthnLayers.prod
}
