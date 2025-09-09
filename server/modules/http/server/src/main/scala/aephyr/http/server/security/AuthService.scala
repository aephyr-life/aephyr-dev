package aephyr.http.server.security

import zio.*
import aephyr.api.shared.AuthenticationContext
import aephyr.kernel.id.UserId
import aephyr.security.Principal

sealed trait AuthError extends Throwable
object AuthError {
  case object InvalidToken extends AuthError
}

trait AuthService {
  def authenticate(ctx: AuthenticationContext): IO[AuthError, Principal]
}

object AuthService {
  // DEV: accept any token; swap for real JWT checks later
  val live: ULayer[AuthService] =
    ZLayer.succeed(
      new AuthService {
        override def authenticate(ctx: AuthenticationContext): IO[AuthError, Principal] =
          ZIO.succeed(
            Principal(UserId.random) // TODO extract UserId
          ) // IO[Nothing, _] widens to IO[AuthError, _]
      }
    )
}
