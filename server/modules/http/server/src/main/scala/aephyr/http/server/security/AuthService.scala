package aephyr.http.server.security

import zio.*
import aephyr.api.shared.AuthenticationContext

sealed trait AuthError extends Throwable
object AuthError {
  case object InvalidToken extends AuthError
}

trait AuthService {
  def authenticate(ctx: AuthenticationContext): IO[AuthError, AuthenticationContext]
}

object AuthService {
  // DEV: accept any token; swap for real JWT checks later
  val live: ULayer[AuthService] =
    ZLayer.succeed(
      new AuthService {
        override def authenticate(ctx: AuthenticationContext): IO[AuthError, AuthenticationContext] =
          ZIO.succeed(ctx) // IO[Nothing, _] widens to IO[AuthError, _]
      }
    )
}
