package aephyr.adapters.db

import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

import scala.language.unsafeNulls

import aephyr.identity.application.ports.UserReadPort
import aephyr.identity.domain.User
import aephyr.identity.domain.User.EmailAddress
import aephyr.kernel.PersistenceError
import zio.{ IO, ZLayer }

import aephyr.kernel.id.UserId

final case class UserReadRepository(ds: DataSource) extends UserReadPort:

  object U {
    val id         = "id"
    val email_norm = "email_norm"
    val status     = "status"
    val created_at = "created_at"
    val updated_at = "updated_at"
  }

  override def findById(userId: UserId): IO[PersistenceError, Option[User]] =
    import U.*

    val sql =
      s"SELECT $id, $email_norm, $status, $created_at, $updated_at " +
        s"FROM read.users_read WHERE $id = ?"

    JdbcMini.withConnection(ds) {
      JdbcMini.queryOne(sql, Seq(Some(userId.value)))(userFromRs)
    }

//  override def findByEmail(
//    email: EmailAddress
//  ): IO[PersistenceError, Option[User]] =
//    import U.*
//
//    val sql =
//      s"SELECT $id, $email_norm, $status, $created_at, $updated_at " +
//        s"FROM read.users_read WHERE $email_norm = ?"
//
//    JdbcMini.withConnection(ds) {
//      JdbcMini.queryOne(sql, Seq(Some(email.normalized)))(userFromRs)
//    }

  private def userFromRs(rs: ResultSet): User = {
    import U.*
    User(
      UserId(rs.getObject(id, classOf[UUID])),
      "",
      "",
      User.Status.valueOf(rs.getString(status).capitalize),
      rs.getTimestamp(created_at).toInstant,
      rs.getTimestamp(updated_at).toInstant
    )
  }

object UserReadRepository:

  val layer: ZLayer[DataSource, Nothing, UserReadPort] =
    ZLayer.fromFunction(UserReadRepository.apply)
