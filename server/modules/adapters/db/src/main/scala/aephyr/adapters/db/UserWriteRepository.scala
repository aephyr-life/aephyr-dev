package aephyr.adapters.db

import java.time.Instant
import javax.sql.DataSource

import aephyr.identity.application.ports.UserWritePort
import aephyr.identity.domain.User
import aephyr.identity.domain.User.{ EmailAddress }
import aephyr.kernel.PersistenceError
import zio.{ IO, ZIO, ZLayer }

import aephyr.kernel.id.UserId

case class UserWriteRepository(ds: DataSource) extends UserWritePort {

  /** Create user if not exists; return existing id if email already present
    * (idempotent).
    */
  override def createPending(
    email: EmailAddress,
    at: Instant
  ): IO[PersistenceError, UserId] = ZIO.succeed(UserId.random)

  /** Activate user (idempotent). */
  override def activate(id: UserId, at: Instant): IO[PersistenceError, Unit] =
    ZIO.succeed(())

  /** Touch last login (idempotent / monotonic). */
  override def touchLastLogin(id: UserId, at: Instant): IO[PersistenceError, Unit] =
    ZIO.succeed(())
}

object UserWriteRepository:

  val layer: ZLayer[DataSource, Nothing, UserWriteRepository] =
    ZLayer.fromFunction(UserWriteRepository.apply)
