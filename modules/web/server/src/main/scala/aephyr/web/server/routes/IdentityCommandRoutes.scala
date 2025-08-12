//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes

import aephyr.identity.api.command.{
  MagicLinkCommandEndpoints,
  MagicLinkCreationResponse
}
import aephyr.identity.application.MagicLinkService
import sttp.tapir.ztapir._
import zio._

object IdentityCommandRoutes:

  val requestMagicLink: ZServerEndpoint[MagicLinkService, Any] =
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
    requestMagicLink
  )
