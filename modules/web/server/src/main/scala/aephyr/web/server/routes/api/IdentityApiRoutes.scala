//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes.api

import aephyr.identity.application.MagicLinkService
import aephyr.identity.api.query.*
import aephyr.identity.api.command.*
import aephyr.api.ErrorMappings

import sttp.tapir.ztapir.*
import zio.*
import zio.http.*

object IdentityApiRoutes {

  private val redeemMagicLinkToken: ZServerEndpoint[MagicLinkService, Any] =
    MagicLinkQueryEndpoints.redeemMagicLink.zServerLogic[MagicLinkService] {
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

  val requestMagicLinkToken: ZServerEndpoint[MagicLinkService, Any] =
    MagicLinkCommandEndpoints.requestMagicLink.zServerLogic[MagicLinkService] {
      req =>
        ZIO
          .serviceWithZIO[MagicLinkService] {
            svc =>
              svc.sendMagicLink(
                req.email,
                "192.168.1.1",
                "ua"
              ) // TODO ip und ua
          }
          .as(
            MagicLinkCreationResponse(
              "If the email exists, a magic link has been sent."
            )
          )
    }

  val all = List(
    requestMagicLinkToken, redeemMagicLinkToken
  )
}
