package aephyr.identity.domain.auth

enum AuthError derives CanEqual:

  case InvalidToken
  case DisabledUser
  case Internal(cause: Throwable)
