package aephyr.web.server.routes.browser.identity.handler
import aephyr.web.server.security.SessionCookie
import aephyr.web.server.util.Html
import zio.ZIO
import zio.http.*
object InvalidHandler {
  def apply() = handler {
                val html =
                  """<!doctype html><meta charset="utf-8"><title>Link invalid</title>
                    |<h1>That link can’t be used</h1>
                    |<p>It may have expired or was already used. Request a new one.</p>
                    |<p><a href="/web/auth/login">Back to login</a></p>
                    |""".stripMargin
                ZIO.succeed(Html(html).updateHeaders(_ ++ SessionCookie.securityHeaders))
              }
}
