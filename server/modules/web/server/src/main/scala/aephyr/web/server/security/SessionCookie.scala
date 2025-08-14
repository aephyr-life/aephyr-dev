package aephyr.web.server.security

import aephyr.identity.application.ports.SessionId
import aephyr.shared.config.MagicLinkCfg
import zio.http.*

import java.time.{ZoneId, ZonedDateTime}

object SessionCookie {

  private val epoch = ZonedDateTime.ofInstant(java.time.Instant.EPOCH, ZoneId.of("UTC")).nn

  val securityHeaders: Headers =
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

  /** Build a secure cookie for the given session id, using settings from MagicLinkCfg. */
  def make(id: SessionId, cfg: MagicLinkCfg): Cookie.Response =
    Cookie.Response(
      name       = cfg.cookie.name,
      content    = id.value,
      domain     = Option(cfg.cookie.domain),
      path       = Option(cfg.cookie.path).orElse(Some("/")).map(Path.apply),
      isSecure   = cfg.cookie.secure,
      isHttpOnly = cfg.cookie.httpOnly,
      sameSite   = Option(cfg.cookie.sameSite.toLowerCase.nn) match {
        case Some("strict") => Some(Cookie.SameSite.Strict)
        case Some("none")   => Some(Cookie.SameSite.None)
        case _              => Some(Cookie.SameSite.Lax)
      },
      maxAge     = cfg.cookie.maxAgeSec.map(zio.Duration.fromSeconds(_))
    )
}
