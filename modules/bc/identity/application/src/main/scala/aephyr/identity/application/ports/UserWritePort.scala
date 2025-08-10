package aephyr.identity.application.ports

import zio.*
import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import java.time.Instant

/**
 * This will be replaced with Ean event storage eventually.
 */
trait UserWritePort {
  /** Create user if not exists; return existing id if email already present (idempotent). */
  def createPending(email: User.EmailAddress, at: Instant): IO[PersistenceError, User.Id]

  /** Activate user (idempotent). */
  def activate(id: User.Id, at: Instant): IO[PersistenceError, Unit]

  /** Touch last login (idempotent / monotonic). */
  def touchLastLogin(id: User.Id, at: Instant): IO[PersistenceError, Unit]
}
