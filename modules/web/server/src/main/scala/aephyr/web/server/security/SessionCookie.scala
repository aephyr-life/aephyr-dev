package aephyr.web.server.security

import aephyr.identity.application.ports.SessionId
import aephyr.shared.config.MagicLinkCfg
import zio.http._

object SessionCookie {

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
