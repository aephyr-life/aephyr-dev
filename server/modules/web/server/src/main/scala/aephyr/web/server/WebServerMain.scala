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

import aephyr.adapters.db.{DataSourceLayer, UserReadRepository, UserWriteRepository}

import aephyr.adapters.security.SecureRandomLive
import aephyr.identity.api.command.IdentityCommandEndpoints
import aephyr.identity.application.ports.TokenStore
import aephyr.shared.config.AppConfig
import aephyr.web.server.routes.api.ApiRoutes
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



  private val app = (ApiRoutes.routes)

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
            UserReadRepository.layer,
            UserWriteRepository.layer,
            AppConfig.layer,
            AppConfig.db,
            SecureRandomLive.layer,
            ZLayer.succeed(Clock.ClockLive),
            DataSourceLayer.live,
          )
          .onInterrupt(ZIO.logInfo("📥 Interrupt received, stopping..."))
      } yield ()
    }
