package aephyr.web.server.routes.browser.identity.handler

import aephyr.shared.config.MagicLinkCfg
import zio.ZIO
import zio.http.*

object LogoutHandler {
//  def apply() = handler { (req: Request) =>
//    for {
//      cfg <- ZIO.service[MagicLinkCfg]
//      sidO = req.cookies.get(cfg.cookie.name).map(c => SessionId(c.content))
//      _ <- ZIO.foreachDiscard(sidO)(sid => ZIO.serviceWithZIO[SessionService](_.revoke(sid)))
//      // expire cookie on client
//      expired = SessionCookie.make(SessionId(""), cfg).copy(maxAge = Some(Duration.Zero))
//    } yield Response.redirect(URL.decode("/web/auth/login").toOption.get, false)
//      .addCookie(expired)
//      .updateHeaders(_ ++ securityHeaders)
//  }
}
