package aephyr.identity.domain.auth

enum AuthError derives CanEqual:

  case InvalidToken
  case MissingCredentials
  case TokenExpired
  case UserDisabled
  case UserNotFound
  case Internal(cause: Throwable)
