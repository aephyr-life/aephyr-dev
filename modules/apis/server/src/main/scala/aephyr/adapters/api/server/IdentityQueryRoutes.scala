package aephyr.adapters.api.server

import aephyr.identity.api.query.{MagicLinkConsumptionResponse, MagicLinkQueryEndpoints}
import aephyr.identity.application.MagicLinkService
import aephyr.api.ErrorMappings
import sttp.tapir.ztapir.*
import zio.*

object IdentityQueryRoutes:

  private val consumeMagicLink: ZServerEndpoint[MagicLinkService, Any] =
    MagicLinkQueryEndpoints.consumeMagicLink.zServerLogic[MagicLinkService] { req =>
      ZIO.serviceWithZIO[MagicLinkService](_.consumeMagicLink(req))
        .mapError(ErrorMappings.fromAuth)
        .as(MagicLinkConsumptionResponse())
    }


  val all = List(
    consumeMagicLink
  )
