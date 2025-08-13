package aephyr.web.server.routes.browser.identity.handler

import zio.*
import zio.http.*

import aephyr.web.server.security.SessionCookie

object LoginFormHandler {
  def apply(): Handler[Any, Nothing, Any, Response] = handler {
    val html =
      """<!doctype html><meta charset="utf-8"><title>Sign in</title>
        |<h1>Sign in</h1>
        |<form method="post" action="/web/auth/magic/request">
        |  <label>Email<br><input type="email" name="email" required autocomplete="email" autofocus></label>
        |  <button type="submit">Send magic link</button>
        |</form>
        |""".stripMargin
    ZIO.succeed(Response.text(html).updateHeaders(_ ++ SessionCookie.securityHeaders))
    }
  }


//
//          // 3) Check-your-email page
//          Method.GET / "web" / "auth" / "magic" / "check" ->
//            handler {
//              val html =
//                """<!doctype html><meta charset="utf-8"><title>Check your email</title>
//                  |<h1>Check your email</h1>
//                  |<p>If an account exists for that address, we sent a sign-in link. It expires soon.</p>
//                  |<p><a href="/web/auth/login">Back</a></p>
//                  |""".stripMargin
//              ZIO.succeed(Response.text(html).updateHeaders(_ ++ securityHeaders))
//            },
//

//
//          // 5) POST redeem: consume token, set cookie, redirect to app
//          Method.POST / "web" / "auth" / "magic" / "redeem" ->
//            handler { (req: Request) =>
//              val Z = ZIO
//              val tokenEff =
//                req.body.asURLEncodedForm.flatMap { form =>
//                  form.get("token") match
//                    case Some(FormField.Simple(v)) if v.nonEmpty => Z.succeed(v)
//                    case _ => Z.fail(new Exception("missing_token"))
//                }
//
//              (for {
//                token <- tokenEff
//                userId <- Z.serviceWithZIO[MagicLinkService](_.consumeMagicLink(token)).mapError {
//                  case AuthError.InvalidToken => new Exception("invalid")
//                  case AuthError.DisabledUser => new Exception("disabled")
//                  case AuthError.Internal(e) => e
//                }
//                sess <- Z.serviceWithZIO[SessionService](_.create(userId))
//                cfg <- Z.service[MagicLinkCfg]
//                cookie = SessionCookie.make(sess.id, cfg)
//              } yield Response
//                .redirect(toURL(cfg.redirects.successUrl), false)
//                .addCookie(cookie)
//                .updateHeaders(_ ++ securityHeaders)
//                ).catchAll { _ =>
//                Z.serviceWith[MagicLinkCfg](c => toURL(c.redirects.errorUrl))
//                  .map(url => Response.redirect(url, false).updateHeaders(_ ++ securityHeaders))
//              }
//            },
//
//
//
//          // 7) Logout (optional)
//          Method.POST / "web" / "auth" / "logout" ->
//            handler { (req: Request) =>
//              for {
//                cfg <- ZIO.service[MagicLinkCfg]
//                sidO = req.cookies.get(cfg.cookie.name).map(c => SessionId(c.content))
//                _ <- ZIO.foreachDiscard(sidO)(sid => ZIO.serviceWithZIO[SessionService](_.revoke(sid)))
//                // expire cookie on client
//                expired = SessionCookie.make(SessionId(""), cfg).copy(maxAge = Some(Duration.Zero))
//              } yield Response.redirect(URL.decode("/web/auth/login").toOption.get, false)
//                .addCookie(expired)
//                .updateHeaders(_ ++ securityHeaders)
//            }
//        )

//      private def escapeHtml(s: String): String =
//        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
//    }
//  }
//}
