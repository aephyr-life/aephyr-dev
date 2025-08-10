package aephyr.adapters.api.server

import aephyr.adapters.db.{DataSourceLayer, TokenStoreLive, UserReadRepository, UserWriteRepository}
import aephyr.adapters.messaging.EmailSenderLive
import aephyr.identity.application.MagicLinkConfig
import aephyr.identity.application.ports.TokenStore
import aephyr.identity.application.{MagicLinkService, MagicLinkServiceLive}
import aephyr.adapters.security.SecureRandomLive
import aephyr.config.{AppConfig, MagicLinkCfg}
import zio.*
import zio.Clock
import zio.http.*
import zio.logging.backend.SLF4J
import zio.logging.loggerName

object ApiServerMain extends ZIOAppDefault:

  override val bootstrap: ULayer[Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  override def run: ZIO[Any, Throwable, Unit] =
    ZIO.scoped {
      for {
        _ <- ZIO.addFinalizer(ZIO.logInfo("🛑 Shutting down server..."))
        _ <- Server
          .serve(ApiRoutes.all)
          .provide(
            Server.defaultWithPort(8080),
            MagicLinkServiceLive.layer,
            TokenStoreLive.layer,
            EmailSenderLive.layer,
            UserReadRepository.layer,
            UserWriteRepository.layer,
            AppConfig.layer,
            MagicLinkCfg.layer,
            SecureRandomLive.layer,
            ZLayer.succeed(Clock.ClockLive),
            DataSourceLayer.live
          )
          .onInterrupt(ZIO.logInfo("📥 Interrupt received, stopping..."))
      } yield ()
    } @@ loggerName("zio.http.Server")
