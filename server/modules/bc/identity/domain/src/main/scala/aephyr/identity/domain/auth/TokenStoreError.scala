package aephyr.identity.domain.auth

enum TokenStoreError(message: String, cause: Throwable | Null = null)
  extends Exception(message, cause) derives CanEqual:

  case InvalidOrExpired
    extends TokenStoreError(
      "token invalid or expired."
    )

  case DbError(cause: Throwable)
    extends TokenStoreError(
      s"""db error: "${cause.getMessage}"""",
      cause
    )
