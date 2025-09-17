package aephyr.identity.application.ports

import java.time.Instant
import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import zio.*
import aephyr.kernel.id.UserId

/** This will be replaced with Ean event storage eventually.
  */
trait UserWritePort {

  def upsertActive(id: UserId, at: Instant): IO[PersistenceError, User]

  /** Touch last login (idempotent / monotonic). */
  def touchLastLogin(id: UserId, at: Instant): IO[PersistenceError, Unit]
}

object UserWritePort {

  def upsertActive(id: UserId, at: Instant): ZIO[UserWritePort, PersistenceError, User] =
    ZIO.serviceWithZIO[UserWritePort](_.upsertActive(id, at))

  def touchLastLogin(id: UserId, at: Instant): ZIO[UserWritePort, PersistenceError, Unit] =
    ZIO.serviceWithZIO[UserWritePort](_.touchLastLogin(id, at))
}
