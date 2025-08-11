package aephyr.identity.domain.auth

sealed trait TokenError extends Throwable

object TokenError:

  case object InvalidOrExpired extends TokenError:
    override def getMessage: String = "token invalid or expired."

  final case class DbError(cause: Throwable) extends TokenError:
    override def getMessage: String = s"db error: \"${cause.getMessage}\""
    override def getCause: Throwable = cause


