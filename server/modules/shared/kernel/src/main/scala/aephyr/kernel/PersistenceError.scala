package aephyr.kernel

sealed trait PersistenceError extends Throwable

object PersistenceError {
  
  final case class OptimisticLock(expected: Long, actual: Long)
    extends Exception(s"version conflict: expected=$expected actual=$actual")
    with PersistenceError

  final case class UniqueViolation(msg: String)
    extends Exception(msg)
    with PersistenceError

  final case class ForeignKeyViolation(msg: String)
    extends Exception(msg)
    with PersistenceError

  final case class SerializationFailure(msg: String)
    extends Exception(msg)
    with PersistenceError

  final case class DeadlockDetected(msg: String)
    extends Exception(msg)
    with PersistenceError

  final case class CheckViolation(msg: String)
    extends Exception(msg)
    with PersistenceError

  final case class Timeout(msg: String)
    extends Exception(msg)
    with PersistenceError

  final case class Unknown(msg: String, cause: Throwable | Null = null)
    extends Exception(msg, cause)
    with PersistenceError
}
