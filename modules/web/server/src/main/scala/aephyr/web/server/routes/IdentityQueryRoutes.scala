//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes

import aephyr.api.ErrorMappings
import aephyr.identity.api.query.{
  MagicLinkConsumptionResponse,
  MagicLinkQueryEndpoints
}
import aephyr.identity.application.MagicLinkService
import sttp.tapir.ztapir._
import zio._

object IdentityQueryRoutes:

  private val consumeMagicLink: ZServerEndpoint[MagicLinkService, Any] =
    MagicLinkQueryEndpoints.consumeMagicLink.zServerLogic[MagicLinkService] {
      req =>
        ZIO
          .serviceWithZIO[MagicLinkService](_.consumeMagicLink(req))
          .mapError(ErrorMappings.fromAuth)
          .as(
            MagicLinkConsumptionResponse(
              "If an account exists for this email, " +
                "a sign-in link has been sent."
            )
          )
    }

  val all = List(
    consumeMagicLink
  )
