package aephyr.http.server

import zio.*
import zio.http.Server
import zio.logging.backend.SLF4J
import aephyr.http.server.endpoint.HttpRoutes
import aephyr.http.server.wiring.HttpAppLayers
import aephyr.shared.config.AppConfig

object HttpServer extends ZIOAppDefault {

  override val bootstrap: ULayer[Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private def program(port: Int): ZIO[HttpAppLayers.Env, Throwable, Unit] =
    Server.serve(HttpRoutes.routes).provide(Server.defaultWithPort(port))

  def run: ZIO[ZIOAppArgs & Scope, Nothing, Nothing] =
    ZIO.scoped {
      for {
        _    <- ZIO.addFinalizer(ZIO.logInfo("🛑 shutting down..."))
        cfg  <- ZIO.service[AppConfig]
        port  = cfg.http.port
        _    <- program(port)
          .provideSomeLayer(HttpAppLayers.dev)
          .onStart(ZIO.logInfo(s"🚀 HTTP on :$port"))
          .onInterrupt(ZIO.logInfo("📥 interrupt received"))
      } yield ()
    }
}
