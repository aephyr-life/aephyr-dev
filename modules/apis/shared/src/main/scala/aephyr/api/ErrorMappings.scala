package aephyr.api

import aephyr.identity.domain.auth.AuthError
import sttp.model.StatusCode

object ErrorMappings:

  def fromAuth(e: AuthError): ErrorDto = e match {
    case AuthError.InvalidToken =>
      ErrorDto("invalid_token", "The link is invalid or expired.", StatusCode.Unauthorized.code)
    case AuthError.DisabledUser =>
      ErrorDto("disabled_user", "The user account is disabled.", StatusCode.Forbidden.code)
    case AuthError.Internal(cause) =>
      ErrorDto("internal_error", s"An unexpected error occurred: ${cause.getMessage}", StatusCode.InternalServerError.code)
  }
