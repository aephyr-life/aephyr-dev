package aephyr.identity.application

import aephyr.identity.domain.User
import aephyr.identity.domain.auth.AuthError
import zio.{ IO, Trace, UIO }

trait MagicLinkService:

  def sendMagicLink(reqEmail: User.EmailAddress, clientIp: String, ua: String)(
    using Trace
  ): UIO[Unit]

  def consumeMagicLink(
    token: String
  ): IO[AuthError, User] // TODO replace String with opaque type
