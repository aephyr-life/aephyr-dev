package aephyr.web.server.routes

import aephyr.identity.application.MagicLinkService
import aephyr.identity.domain.auth.AuthError
import aephyr.config.MagicLinkCfg
import aephyr.shared.security.SecureRandom
import zio.*
import zio.http.*
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
        case "lax" => Some(Cookie.SameSite.Lax)
        case "strict" => Some(Cookie.SameSite.Strict)
        case "none" => Some(Cookie.SameSite.None)
        case _ => Some(Cookie.SameSite.Lax),
      maxAge = cfg.cookie.maxAgeSec.map(Duration.fromSeconds(_))
    )

  def routes: Routes[MagicLinkService & MagicLinkCfg & SecureRandom, Nothing] =
    Routes(
      Method.GET / "web" / "auth" / "link" / PathCodec.string("token") ->
        handler { (token: String, req: Request) =>
          val Z = ZIO
          (for {
            cfg <- Z.service[MagicLinkCfg]
            _   <- Z.serviceWithZIO[MagicLinkService](_.consumeMagicLink(token)).mapError {
              case AuthError.InvalidToken => new Exception("invalid")
              case AuthError.DisabledUser => new Exception("disabled")
              case AuthError.Internal(e)  => e
            }
            sid <- Z.serviceWithZIO[SecureRandom](_.nextBytesAsBase64(32))
          } yield Response
            .redirect(URL.decode(cfg.redirects.successUrl).right.get, false)
            .addCookie(cookieResponse(sid, cfg))
            ).catchAll { _ =>
            Z.serviceWith[MagicLinkCfg](c => URL.decode(c.redirects.errorUrl).right.get)
              .map(url => Response.redirect(url, false))
          }
        }
    )