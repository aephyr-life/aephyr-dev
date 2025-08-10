package aephyr.adapters.db

import aephyr.identity.application.ports.{UserReadPort, UserWritePort}
import aephyr.identity.domain.User.{EmailAddress, Id}
import aephyr.kernel.PersistenceError
import zio.{IO, ZLayer}

import java.time.Instant
import javax.sql.DataSource

case class UserWriteRepository(ds: DataSource) extends UserWritePort {
  
  /** Create user if not exists; return existing id if email already present (idempotent). */
  override def createPending(email: EmailAddress, at: Instant): IO[PersistenceError, Id] = ???

  /** Activate user (idempotent). */
  override def activate(id: Id, at: Instant): IO[PersistenceError, Unit] = ???

  /** Touch last login (idempotent / monotonic). */
  override def touchLastLogin(id: Id, at: Instant): IO[PersistenceError, Unit] = ???
}

object UserWriteRepository:

  val layer: ZLayer[DataSource, Nothing, UserWriteRepository] =
    ZLayer.fromFunction(UserWriteRepository.apply)
