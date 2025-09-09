package aephyr.http.server.wiring

import aephyr.http.server.app.identity.MeService
import zio.*
import aephyr.shared.config.{AasaCfg, AppConfig, DbCfg}
import aephyr.http.server.endpoint.HttpTypes
import aephyr.http.server.security.AuthService
import aephyr.http.server.wiring.infra.InfraLayers
import aephyr.http.server.wiring.identity.WebAuthnLayers

object HttpAppLayers {
  type Env = HttpTypes.Env // = PublicEnv & IdentityEnv & SecurityEnv

  // Inputs per your graph (adjust if InfraLayers now takes only AppConfig)
  private type In = DbCfg & AasaCfg & (AppConfig & Clock)

  private val identityLayers: ZLayer[In, Throwable, HttpTypes.IdentityEnv] =
    (InfraLayers.live >>> MeService.live) ++
      WebAuthnLayers.dev

  private val publicAndSecurity: ZLayer[In, Throwable, HttpTypes.PublicEnv & HttpTypes.SecurityEnv] =
    // pass-through AasaCfg AND provide AuthService together (no cycle)
    (ZLayer.service[AasaCfg] ++ AuthService.live)

  val dev: ZLayer[In, Throwable, Env] =
    identityLayers ++ publicAndSecurity
}
