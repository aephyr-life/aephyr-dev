//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.web.server.routes.browser

import scala.language.unsafeNulls
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

  private val epoch = ZonedDateTime.ofInstant(java.time.Instant.EPOCH, ZoneId.of("UTC"))
  
  private val securityHeaders: Headers =
    Headers(
      Header.CacheControl.NoStore,
      Header.Pragma.NoCache,
      Header.Expires(epoch),
      Header.Custom("Referrer-Policy", "no-referrer"),
      Header.Custom("X-Content-Type-Options", "nosniff"),
      Header.Custom(
        "Content-Security-Policy",
        "default-src 'none'; base-uri 'none'; form-action 'self'"
      )
    )

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
      Method.POST / "web" / "auth" / "magic" / "redeem" ->
        handler { (req: Request) =>
          val Z = ZIO

          // 1) Try to read token from x-www-form-urlencoded body
          val tokenFromForm: ZIO[Any, Throwable, Option[String]] =
            req.body.asURLEncodedForm.map { form =>
              form.get("token") match
                case Some(FormField.Simple(_, value)) => Some(value)
                case _ =>  None
            }

//          val tokenOpt =
//            req.body.asURLEncodedForm
//              .map(_.get("token").flatMap(_.headOption))

          (for {
            token <- tokenFromForm.flatMap {
              case Some(t) => Z.succeed(t)
              case None    => Z.fail(new Exception("missing_token"))
            }

            // Consume magic link, returning the authenticated userId
            user <- Z.serviceWithZIO[MagicLinkService](_.consumeMagicLink(token))
              .mapError {
                case AuthError.InvalidToken => new Exception("invalid")
                case AuthError.DisabledUser => new Exception("disabled")
                case AuthError.Internal(e)  => e
              }

            // Create a new session
            session <- Z.serviceWithZIO[SessionService](_.create(user.id))

            // Build session cookie
            cfg     <- Z.service[MagicLinkCfg]
            cookie   = SessionCookie.make(session.id, cfg)

          } yield Response
            .redirect(toURL(cfg.redirects.successUrl), false)
            .addCookie(cookie)
            .updateHeaders(_ ++ securityHeaders)
            ).catchAll { _ =>
            Z.serviceWith[MagicLinkCfg](c => toURL(c.redirects.errorUrl))
              .map(url => Response.redirect(url, false).updateHeaders(_ ++ securityHeaders))
          }
        },

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
