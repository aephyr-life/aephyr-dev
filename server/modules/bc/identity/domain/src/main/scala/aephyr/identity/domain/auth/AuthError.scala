package aephyr.identity.domain.auth

enum AuthError derives CanEqual:

  case InvalidToken
  case MissingCredentials
  case TokenExpired
  case UserDisabled
  case UserNotFound
  case NotAvailable(cause: Throwable)
  case Internal(cause: Throwable)
