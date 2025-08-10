package aephyr.identity.domain.auth

sealed trait TokenError extends Throwable

object TokenError:

  case object InvalidOrExpired extends TokenError

  final case class DbError(cause: Throwable) extends TokenError:
    override def getCause: Throwable = cause


