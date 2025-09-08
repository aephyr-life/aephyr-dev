package aephyr.http.server

import zio.*
import zio.http.Server
import aephyr.http.server.endpoint.HttpRoutes
import aephyr.http.server.wiring.HttpAppLayers
import aephyr.shared.config.AppConfig

object HttpServer extends ZIOAppDefault {

  // Provide a live Clock for your ZIO build
  private val clockLayer: ZLayer[Any, Nothing, Clock] =
    ZLayer.succeed(Clock.ClockLive)

  // Base inputs
  private val base: ZLayer[Any, Throwable, AppConfig & Clock] =
    AppConfig.layer ++ clockLayer

  // If InfraLayers still needs DbCfg, keep it; else drop DbCfg from here
  private val httpInputs
  : ZLayer[AppConfig, Nothing, aephyr.shared.config.DbCfg & aephyr.shared.config.AasaCfg & AppConfig] =
    AppConfig.db ++ AppConfig.aasa ++ ZLayer.service[AppConfig]

  private val appEnv: ZLayer[AppConfig & Clock, Throwable, HttpAppLayers.Env] =
    httpInputs >>> HttpAppLayers.dev

  // Provide only Server here; leave HttpAppLayers.Env to outer provide
  private def program(port: Int): ZIO[HttpAppLayers.Env, Throwable, Nothing] =
    Server.serve(HttpRoutes.routes)
      .provideSome[HttpAppLayers.Env](Server.defaultWithPort(port))

  override def run: ZIO[Any, Throwable, Nothing] =
    ZIO.serviceWithZIO[AppConfig](cfg => program(cfg.http.port))
      .provide(
        base,   // AppConfig & Clock
        appEnv  // AasaCfg, MeService, WebAuthnService, repos, etc.
      )
}
