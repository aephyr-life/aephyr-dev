package aephyr.identity.application.ports

import java.time.Instant

import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import zio._

import aephyr.kernel.id.UserId

/** This will be replaced with Ean event storage eventually.
  */
trait UserWritePort {

  /** Create user if not exists; return existing id if email already present
    * (idempotent).
    */
  def createPending(
    email: User.EmailAddress,
    at: Instant
  ): IO[PersistenceError, UserId]

  /** Activate user (idempotent). */
  def activate(id: UserId, at: Instant): IO[PersistenceError, Unit]

  /** Touch last login (idempotent / monotonic). */
  def touchLastLogin(id: UserId, at: Instant): IO[PersistenceError, Unit]
}
