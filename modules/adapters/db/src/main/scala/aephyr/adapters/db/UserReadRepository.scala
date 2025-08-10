package aephyr.adapters.db

import aephyr.identity.application.ports.UserReadPort
import aephyr.identity.domain.User
import aephyr.identity.domain.User.EmailAddress
import aephyr.kernel.PersistenceError
import zio.{IO, ZLayer}

import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

final case class UserReadRepository(ds: DataSource) extends UserReadPort:

  object U {
    val id = "id"
    val email_norm = "email_norm"
    val status = "status"
    val created_at = "created_at"
    val updated_at = "updated_at"
  }

  override def findById(userId: User.Id): IO[PersistenceError, Option[User]] =
    import U.*

    val sql =
      s"SELECT $id, $email_norm, $status, $created_at, $updated_at " +
        s"FROM read.users_read WHERE $id = ?"

    JdbcMini.withConnection(ds) {
      JdbcMini.queryOne(sql, Seq(userId.value))(userFromRs)
    }

  override def findByEmail(email: EmailAddress): IO[PersistenceError, Option[User]] =
    import U.*

    val sql =
      s"SELECT $id, $email_norm, $status, $created_at, $updated_at " +
        s"FROM read.users_read WHERE $email_norm = ?"

    JdbcMini.withConnection(ds) {
      JdbcMini.queryOne(sql, Seq(email.normalized))(userFromRs)
    }

  private def userFromRs(rs: ResultSet): User = {
    import U.*
    User(
      User.Id(rs.getObject(id, classOf[UUID])),
      User.EmailAddress(rs.getString(email_norm)),
      User.Status.valueOf(rs.getString(status).capitalize),
      rs.getTimestamp(created_at).toInstant,
      rs.getTimestamp(updated_at).toInstant
    )
  }

object UserReadRepository:

  val layer: ZLayer[DataSource, Nothing, UserReadPort] =
    ZLayer.fromFunction(UserReadRepository.apply)
