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
import aephyr.adapters.security.webauthn.memory.InMemoryChallengeStore
import aephyr.adapters.security.webauthn.memory.InMemoryRelyingParty
import aephyr.adapters.security.webauthn.memory.InMemoryUserHandleRepo
import aephyr.adapters.security.webauthn.memory.InMemoryWebAuthnRepo
import aephyr.adapters.security.SecureRandomLive
import aephyr.identity.api.command.IdentityCommandEndpoints
import aephyr.identity.application.ports.TokenStore
import aephyr.shared.config.AppConfig
import aephyr.web.server.routes.api.ApiRoutes
import aephyr.web.server.routes.web.StaticRoutes
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.http.*
import zio.logging.backend.SLF4J
import zio.{Clock, *}
import com.typesafe.config.ConfigFactory

object HttpServer extends ZIOAppDefault {

  //  private val tapirHttp = {
  //    val cmd = IdentityCommands.all // TODO maybe rename to Api?
  //    val qry = IdentityQueries.all
  //
  //    val cmdHttp = ZioHttpInterpreter().toHttp(cmd)
  //    val qryHttp = ZioHttpInterpreter().toHttp(qry)
  //
  //    cmdHttp <+> qryHttp
  //  }

  private def addProp(key: String, value: String | Null): Unit = {
    value match {
      case s: String => sys.props(key) = s
      case _ =>
    }
  }

  private def initLoggingProps(): Unit = {
    val c = ConfigFactory.load().nn
    val log = c.getConfig("app.logging").nn
    addProp("LOG_LEVEL", log.getString("level"))
    Option(log.getString("format")) match {
      case Some(s: String) if s == "pretty" => addProp("CONSOLE_APPENDER", "CONSOLE_PLAIN")
      case Some(s: String) if s == "json" => addProp("CONSOLE_APPENDER", "CONSOLE_JSON")
      case x => throw new RuntimeException(s"unkown log format: $x")
    }
  }

  private val app = (ApiRoutes.routes)

  /*
  mapError(_ => Response.status(Status.InternalServerError)) @@ Middleware.debug
  */

  override val bootstrap: ULayer[Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  override def run: ZIO[Any, Throwable, Unit] =
    initLoggingProps()
    ZIO.scoped {
      for {
        _ <- ZIO.addFinalizer(ZIO.logInfo("🛑 Shutting down server..."))
        _ <- Server
          .serve(app)
          .provide(
            Server.defaultWithPort(8080), // TODO get Port from config
            UserReadRepository.layer,
            UserWriteRepository.layer,
            InMemoryRelyingParty.live,
            InMemoryChallengeStore.live(),
            InMemoryUserHandleRepo.live,
            InMemoryWebAuthnRepo.live,
            AppConfig.layer,
            AppConfig.db,
            AppConfig.aasa,
            SecureRandomLive.layer,
            ZLayer.succeed(Clock.ClockLive),
            DataSourceLayer.live,
          )
          .onInterrupt(ZIO.logInfo("📥 Interrupt received, stopping..."))
      } yield ()
    }
}
