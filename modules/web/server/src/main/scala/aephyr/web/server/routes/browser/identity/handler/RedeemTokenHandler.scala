package aephyr.web.server.routes.browser.identity.handler

import aephyr.identity.application.MagicLinkService
import aephyr.identity.application.ports.SessionService
import aephyr.identity.domain.auth.AuthError
import aephyr.shared.config.{BaseUrl, MagicLinkCfg}
import aephyr.web.server.routes.browser.IdentityBrowserRoutes.toURL
import aephyr.web.server.security.SessionCookie
import zio.ZIO
import zio.http.{FormField, Request, Response, URL, handler}

object RedeemTokenHandler {

  private def toURL(u: BaseUrl): URL =
    URL.decode(u.value).toOption.get

  def apply() =  handler { (req: Request) =>
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
      .updateHeaders(_ ++ SessionCookie.securityHeaders)
      ).catchAll { _ =>
      Z.serviceWith[MagicLinkCfg](c => toURL(c.redirects.errorUrl))
        .map(url => Response.redirect(url, false).updateHeaders(_ ++ SessionCookie.securityHeaders))
    }
  }
}
