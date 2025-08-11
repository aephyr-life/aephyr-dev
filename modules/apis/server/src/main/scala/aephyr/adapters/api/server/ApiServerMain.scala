package aephyr.adapters.api.server

import aephyr.adapters.db.{
  DataSourceLayer,
  TokenStoreLive,
  UserReadRepository,
  UserWriteRepository
}
import aephyr.adapters.messaging.EmailSenderLive
import aephyr.adapters.security.SecureRandomLive
import aephyr.config.{ AppConfig, MagicLinkCfg }
import aephyr.identity.application.ports.TokenStore
import aephyr.identity.application.{ MagicLinkService, MagicLinkServiceLive }
import zio.http._
import zio.logging.backend.SLF4J
import zio.{ Clock, _ }

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
            Server.defaultWithPort(8080), // TODO get Port from config
            MagicLinkServiceLive.layer,
            TokenStoreLive.layer,
            EmailSenderLive.layer,
            UserReadRepository.layer,
            UserWriteRepository.layer,
            AppConfig.layer,
            AppConfig.db,
            AppConfig.magicLink,
            SecureRandomLive.layer,
            ZLayer.succeed(Clock.ClockLive),
            DataSourceLayer.live,
            ZLayer.fromZIO {
              for {
                cfg <- ZIO.service[MagicLinkCfg]
                lb = new LinkBuilder(cfg.baseUrl)
              } yield (token: String) => lb.magicLink(token)
            }
          )
          .onInterrupt(ZIO.logInfo("📥 Interrupt received, stopping..."))
      } yield ()
    }
