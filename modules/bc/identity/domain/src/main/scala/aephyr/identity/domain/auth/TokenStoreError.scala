package aephyr.identity.domain.auth

sealed trait TokenStoreError extends Throwable

object TokenStoreError:

  case object InvalidOrExpired extends TokenStoreError:
    override def getMessage: String = "token invalid or expired."

  final case class DbError(cause: Throwable) extends TokenStoreError:
    override def getMessage: String = s"db error: \"${cause.getMessage}\""
    override def getCause: Throwable = cause


