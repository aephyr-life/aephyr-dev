package aephyr.http.server.wiring

import zio.*
import aephyr.http.server.endpoint.HttpHandler
import aephyr.http.server.endpoint.HttpTypes
import infra.InfraLayers
import identity.MeLayers
import identity.WebAuthnLayers

object HttpAppLayers {

  /** Final environment required by HttpRoutes.routes. */
  type Env = HttpTypes.Env 

  /** Profile-based assembly (dev here; add staging/prod later). */
  val dev: ZLayer[Any, Throwable, Env] =
    InfraLayers.live ++
      MeLayers.dev ++
      WebAuthnLayers.dev

  // val prod: ZLayer[Any, Throwable, Env] = InfraLayers.live ++ IdentityLayers.prod ++ WebAuthnLayers.prod
}
