package aephyr.web.server.routes.browser.identity.handler

import zio.*
import zio.http.*
import aephyr.web.server.security.SessionCookie
import aephyr.identity.application.MagicLinkService
import aephyr.identity.domain.User.EmailAddress

object RequestMagicLinkHandler {
  def apply(): Handler[MagicLinkService, Nothing, Request, Response] =
    Handler.fromFunctionZIO[Request] { (req: Request) =>
      val Z = ZIO
      val emailEff =
        req.body.asURLEncodedForm.flatMap { form =>
          form.get("email") match
            case Some(FormField.Simple(_, v)) if v.nonEmpty => Z.succeed(v.trim.nn)
            case _ => Z.fail(new Exception("missing_email"))
        }

      (for {
        email <- emailEff
        _ <- Z.serviceWithZIO[MagicLinkService](_.sendMagicLink(
          EmailAddress(email), None, None
        )).ignore // TODO rename send -> request
        // Always redirect to check page (no account enumeration)
      } yield Response.seeOther(URL.decode("/web/auth/magic/check").toOption.get)
        .updateHeaders(_ ++ SessionCookie.securityHeaders)
        ).catchAll { _ =>
        // On malformed submission, send back to login
        Z.succeed(Response.seeOther(URL.decode("/web/auth/login").toOption.get)
          .updateHeaders(_ ++ SessionCookie.securityHeaders))
      }
    }
}
