package aephyr.identity.domain.auth

sealed trait AuthError extends Throwable

object AuthError:

  case object InvalidToken extends AuthError

  case object DisabledUser extends AuthError

  final case class Internal(cause: Throwable) extends AuthError:
    override def getCause: Throwable = cause
