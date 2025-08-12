//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes.browser

import scala.language.unsafeNulls

import aephyr.identity.application.MagicLinkService
import aephyr.identity.domain.auth.AuthError
import aephyr.shared.config.{ BaseUrl, MagicLinkCfg }
import aephyr.shared.security.SecureRandom
import zio._
import zio.http._
import zio.http.codec.PathCodec

object IdentityBrowserRoutes:

  def cookieResponse(sid: String, cfg: MagicLinkCfg): Cookie.Response =
    Cookie.Response(
      name = cfg.cookie.name,
      content = sid,
      domain = Option(cfg.cookie.domain),
      path = Option(cfg.cookie.path).orElse(Some("/")).map(Path.apply),
      isSecure = cfg.cookie.secure,
      isHttpOnly = cfg.cookie.httpOnly,
      sameSite = cfg.cookie.sameSite.toLowerCase match
        case "lax"    => Some(Cookie.SameSite.Lax)
        case "strict" => Some(Cookie.SameSite.Strict)
        case "none"   => Some(Cookie.SameSite.None)
        case _        => Some(Cookie.SameSite.Lax),
      maxAge = cfg.cookie.maxAgeSec.map(Duration.fromSeconds(_))
    )

  def routes: Routes[MagicLinkService & MagicLinkCfg & SecureRandom, Nothing] =
    Routes(
      Method.GET / "web" / "auth" / "link" / PathCodec.string("token") ->
        handler {
          (token: String, _: Request) =>
            val Z = ZIO
            (for {
              cfg <- Z.service[MagicLinkCfg]
              _ <- Z
                .serviceWithZIO[MagicLinkService](_.consumeMagicLink(token))
                .mapError {
                  case AuthError.InvalidToken => new Exception("invalid")
                  case AuthError.DisabledUser => new Exception("disabled")
                  case AuthError.Internal(e)  => e
                }
              sid <- Z.serviceWithZIO[SecureRandom](_.nextBytesAsBase64(32))
            } yield Response
              .redirect(toURL(cfg.redirects.successUrl), false)
              .addCookie(cookieResponse(sid, cfg))).catchAll {
              _ =>
                Z.serviceWith[MagicLinkCfg](
                  c => toURL(c.redirects.errorUrl)
                ).map(
                  url => Response.redirect(url, false)
                )
            }
        }
    )

  private def toURL(u: BaseUrl): URL =
    // .get is safe here, BaseUrl already checks URLs
    URL.decode(u.value).toOption.get
