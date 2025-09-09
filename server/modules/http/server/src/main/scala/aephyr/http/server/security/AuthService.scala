package aephyr.http.server.security

import zio.*
import aephyr.api.shared.AuthenticationContext
import aephyr.auth.ports.JwtVerifier
import aephyr.identity.application.ports.UserReadPort
import aephyr.identity.domain.User
import aephyr.identity.domain.auth.AuthError
import aephyr.security.Principal

trait AuthService {
  def authenticate(ctx: AuthenticationContext): IO[AuthError, Principal]
}

object AuthService {
  val live: URLayer[JwtVerifier & UserReadPort, AuthService] =
    ZLayer.fromFunction { (verifier: JwtVerifier, users: UserReadPort) =>
      new AuthService {
        def authenticate(ctx: AuthenticationContext): IO[AuthError, Principal] =
          for {
            token <- ZIO.fromOption(ctx.bearer.map(_.value))
                        .mapError(_ => AuthError.MissingCredentials)
            v     <- verifier.verifyAccess(token)
            user  <- users
                        .findById(v.userId)
                        .mapError(_ => AuthError.InvalidToken)
                        .someOrFail(AuthError.UserNotFound)
            _     <- ZIO.fail(AuthError.UserDisabled)
                        .when(user.status != User.Status.Active)
          } yield Principal(user.id) // , v.roles, user.status
      }
    }
}
