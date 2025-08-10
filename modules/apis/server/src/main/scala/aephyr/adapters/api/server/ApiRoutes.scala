package aephyr.adapters.api.server

import aephyr.identity.api.command.IdentityCommandEndpoints
import aephyr.identity.api.query.IdentityQueryEndpoints
import aephyr.identity.application.MagicLinkService
import sttp.tapir.redoc.RedocUIOptions
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.*
import zio.http.*

object ApiRoutes:

  private val zioHttpInterpreter = ZioHttpInterpreter()

  private val allEndpoints =
    IdentityCommandEndpoints.all ++
      IdentityQueryEndpoints.all

  private val commandRoutes = zioHttpInterpreter.toHttp(
    IdentityCommandRoutes.all
  )

  private val queryRoutes = zioHttpInterpreter.toHttp(
    IdentityQueryRoutes.all
  )

  private val redocEndpoints =
    RedocInterpreter(
      redocUIOptions = RedocUIOptions.default.copy(
        pathPrefix = List("api")
      )
    ).fromEndpoints[zio.Task](allEndpoints, "Aephyr API", "v0")

  private val redocRoute: Routes[Any, Response] =
    zioHttpInterpreter.toHttp(redocEndpoints)

  private val healthRoute =
    Routes(
      Method.HEAD / "api" / "health" -> handler { (_: Request) =>
        Response.text("OK.\n")
      },
      Method.GET / "api" / "health" -> handler { (_: Request) =>
        Response.text("OK.\n")
      }
    )


  val all: Routes[MagicLinkService, Response] =
    healthRoute ++ redocRoute ++ commandRoutes ++ queryRoutes