package aephyr.web.server.routes.browser.identity.handler

import aephyr.web.server.security.SessionCookie
import aephyr.web.server.util.Html
import zio.ZIO
import zio.http.*

object CheckYourMailHandler {
  def apply() = handler {
    val html =
      """<!doctype html><meta charset="utf-8"><title>Check your email</title>
        |<h1>Check your email</h1>
        |<p>If an account exists for that address, we sent a sign-in link. It expires soon.</p>
        |<p><a href="/web/auth/login">Back</a></p>
        |""".stripMargin
    ZIO.succeed(Html(html).updateHeaders(_ ++ SessionCookie.securityHeaders))
  }
}
