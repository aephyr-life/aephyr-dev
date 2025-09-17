package aephyr.adapters.db

import java.time.Instant
import javax.sql.DataSource
import aephyr.identity.application.ports.UserWritePort
import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import zio.{IO, ZIO, ZLayer}
import aephyr.kernel.id.UserId

case class UserWriteRepository(ds: DataSource) extends UserWritePort {

  /** Touch last login (idempotent / monotonic). */
  override def touchLastLogin(id: UserId, at: Instant): IO[PersistenceError, Unit] =
    ZIO.succeed(())

  override def upsertActive(id: UserId, at: Instant): IO[PersistenceError, User] = ???
}

object UserWriteRepository:

  val layer: ZLayer[DataSource, Nothing, UserWriteRepository] =
    ZLayer.fromFunction(UserWriteRepository.apply)
