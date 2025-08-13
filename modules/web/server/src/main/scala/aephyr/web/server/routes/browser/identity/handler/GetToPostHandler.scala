package aephyr.web.server.routes.browser.identity.handler
import zio.ZIO
import zio.http.*

import aephyr.web.server.security.SessionCookie

object GetToPostHandler {
  def apply() =
    handler { (req: Request) =>
      val tokenOpt = req.url.queryParams.getAll("token").headOption
      val html = tokenOpt match {
        case Some(token) if (token.nonEmpty) =>
          s"""<!doctype html><meta charset="utf-8"><title>Signing you in…</title>
             |<form id="f" method="post" action="/web/auth/magic/redeem">
             |  <input type="hidden" name="token" value="${escapeHtml(token)}">
             |</form>
             |<script>document.getElementById('f').submit()</script>
             |<noscript><button form="f">Continue</button></noscript>
             |""".stripMargin
        case _ =>
          s"""<!doctype html><meta charset="utf-8"><title>Invalid</title>
             |<h1>Link invalid</h1>
             |""".stripMargin
      }
      ZIO.succeed(Response.text(html).updateHeaders(_ ++ SessionCookie.securityHeaders))
    }
}

  private def escapeHtml(s: String): String =
          s
            .replace("&", "&amp;").nn
            .replace("<", "&lt;").nn
            .replace(">", "&gt;").nn
            .replace("\"", "&quot;").nn

