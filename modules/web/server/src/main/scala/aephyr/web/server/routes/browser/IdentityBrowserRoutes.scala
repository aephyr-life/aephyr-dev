//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes.browser

import scala.language.unsafeNulls
import aephyr.web.server.routes.browser.identity.handler.*
import aephyr.identity.application.MagicLinkService
import aephyr.identity.application.ports.SessionService
import aephyr.web.server.security.SessionCookie
import aephyr.identity.domain.auth.AuthError
import aephyr.shared.config.{BaseUrl, MagicLinkCfg}
import aephyr.shared.security.SecureRandom
import zio.*
import zio.http.*
import zio.http.codec.PathCodec

import java.time.ZonedDateTime
import java.time.ZoneId

object IdentityBrowserRoutes:

  // TODO move to SessionCookie
  def cookieResponse(sid: String, cfg: MagicLinkCfg): Cookie.Response =
    Cookie.Response(
      name = cfg.cookie.name,
      content = sid,
      domain = Option(cfg.cookie.domain),
      path = Option(cfg.cookie.path).orElse(Some("/")).map(Path.apply),
      isSecure = cfg.cookie.secure,
      isHttpOnly = cfg.cookie.httpOnly,
      sameSite = Option(cfg.cookie.sameSite.toLowerCase) match
        case Some("lax")    => Some(Cookie.SameSite.Lax)
        case Some("strict") => Some(Cookie.SameSite.Strict)
        case Some("none")   => Some(Cookie.SameSite.None)
        case _        => Some(Cookie.SameSite.Lax),
      maxAge = cfg.cookie.maxAgeSec.map(Duration.fromSeconds(_))
    )

  def routes: Routes[SessionService & MagicLinkService & MagicLinkCfg & SecureRandom, Nothing] =
    Routes(
      Method.GET  / "web" / "auth" / "login" -> LoginFormHandler(),
      Method.POST / "web" / "auth" / "magic" / "request" -> RequestMagicLinkHandler(),
      Method.GET  / "web" / "auth" / "magic" / "check" -> CheckYourMailHandler(),
      Method.GET  / "web" / "auth" / "magic" / "land"  -> GetToPostHandler(),
      Method.POST / "web" / "auth" / "magic" / "redeem" -> RedeemTokenHandler(),
      Method.GET  / "web" / "auth" / "magic" / "invalid" -> InvalidHandler(),
//      Method.POST / "web" / "auth" / "logout" -> LogoutHandler(),
    )

  private def toURL(u: BaseUrl): URL =
    URL.decode(u.value).toOption.get
