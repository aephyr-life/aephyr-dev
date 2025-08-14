//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server

import aephyr.adapters.db.{DataSourceLayer, TokenStoreLive, UserReadRepository, UserWriteRepository}
import aephyr.adapters.messaging.EmailSenderLive
import aephyr.adapters.security.SecureRandomLive
import aephyr.adapters.security.session.InMemorySessionService
import aephyr.identity.api.command.IdentityCommandEndpoints
import aephyr.identity.application.ports.TokenStore
import aephyr.identity.application.{MagicLinkService, MagicLinkServiceLive}
import aephyr.shared.config.{AppConfig, MagicLinkCfg}
import aephyr.web.server.routes.api.ApiRoutes
import aephyr.web.server.routes.browser.IdentityBrowserRoutes
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.http.*
import zio.logging.backend.SLF4J
import zio.{Clock, *}

object WebServerMain extends ZIOAppDefault:

//  private val tapirHttp = {
//    val cmd = IdentityCommandEndpoints.all // TODO maybe rename to Api?
//    val qry = IdentityQueryEndpoints.all
//
//    val cmdHttp = ZioHttpInterpreter().toHttp(cmd)
//    val qryHttp = ZioHttpInterpreter().toHttp(qry)
//
//    cmdHttp <+> qryHttp
//  }

  private val browserHttp =
    (IdentityBrowserRoutes.routes
      // ++ ProtectedRoutes.routes
      )

  private val app = (browserHttp ++ ApiRoutes.routes)

  /*
  mapError(_ => Response.status(Status.InternalServerError)) @@ Middleware.debug
  */

  override val bootstrap: ULayer[Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  override def run: ZIO[Any, Throwable, Unit] =
    ZIO.scoped {
      for {
        _ <- ZIO.addFinalizer(ZIO.logInfo("🛑 Shutting down server..."))
        _ <- Server
          .serve(app)
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
            AppConfig.magicLinkIssuance,
            SecureRandomLive.layer,
            ZLayer.succeed(Clock.ClockLive),
            DataSourceLayer.live,
            InMemorySessionService.live,
            ZLayer.fromZIO {
              for {
                cfg <- ZIO.service[MagicLinkCfg]
                lb = new LinkBuilder(cfg.links.apiBaseUrl)
              } yield (token: String) => lb.magicLink(token)
            }
          )
          .onInterrupt(ZIO.logInfo("📥 Interrupt received, stopping..."))
      } yield ()
    }
