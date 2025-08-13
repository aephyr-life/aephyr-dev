package aephyr.web.server.routes.browser.identity.handler
import zio.ZIO
import zio.http.*
import aephyr.web.server.security.SessionCookie
import aephyr.web.server.util.Html
import scala.util.Random
import java.time.ZonedDateTime
import java.time.ZoneId

object GetToPostHandler {
  def apply() =
    handler { (req: Request) =>
      val tokenOpt = req.url.queryParams.getAll("token").headOption
      for {
        // per-response nonce; any random string works (uuid here)
        nonce <- ZIO.succeed("asdfgasfasdg") // TODO replace hardcoded nonce
        html = tokenOpt match {
          case Some(token) if token.nonEmpty =>
            // TODO don't hardcode redeem url
            s"""<!doctype html><meta charset="utf-8"><title>Signing you in…</title>
               |<form id="f" method="post" action="/web/auth/magic/redeem">
               |  <input type="hidden" name="token" value="${escapeHtml(token)}">
               |</form>
               |<script nonce="$nonce">document.getElementById('f').submit()</script>
               |<noscript><button form="f">Continue</button></noscript>
               |""".stripMargin
          case _ =>
            """<!doctype html><meta charset="utf-8"><title>Invalid</title>
              |<h1>Link invalid</h1>
              |<p>Request a new sign‑in link.</p>
              |""".stripMargin
        }
      } yield Html(html).updateHeaders(_ ++ securityHeaders(nonce))
    }

  private val epoch = ZonedDateTime.ofInstant(java.time.Instant.EPOCH, ZoneId.of("UTC")).nn

  private def securityHeaders(nonce: String): Headers =
    Headers(
      Header.CacheControl.NoStore,
      Header.Pragma.NoCache,
      Header.Expires(epoch),
      Header.Custom("Referrer-Policy", "no-referrer"),
      Header.Custom("X-Content-Type-Options", "nosniff"),
      Header.Custom(
        "Content-Security-Policy",
        // allow only this inline script; nothing else
        s"default-src 'none'; base-uri 'none'; form-action 'self'; script-src 'nonce-$nonce'"
      )
    )

  private def escapeHtml(s: String): String =
    s
      .replace("&", "&amp;").nn
      .replace("<", "&lt;").nn
      .replace(">", "&gt;").nn
      .replace("\"", "&quot;").nn
}
