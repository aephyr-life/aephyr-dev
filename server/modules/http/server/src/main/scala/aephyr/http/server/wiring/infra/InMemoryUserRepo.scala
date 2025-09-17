package aephyr.http.server.wiring.infra

import aephyr.identity.application.ports.{UserReadPort, UserWritePort}
import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import aephyr.kernel.id.UserId
import aephyr.kernel.userlabels.UserLabels
import zio.{IO, ULayer, ZLayer}
import zio.stm.{TMap, ZSTM}

import java.time.Instant

class InMemoryUserRepo(
                        users: TMap[UserId, User],
                        usernames: TMap[String, UserId]
                      ) extends UserReadPort
  with UserWritePort {

  import aephyr.kernel.id.UserId

  private def ensureUniqueUsername(
                                    base: String
                                  ): ZSTM[Any, Nothing, String] = {
    def loop(i: Int): ZSTM[Any, Nothing, String] = {
      val candidate = if (i == 0) base else s"$base-$i"
      usernames.get(candidate).flatMap {
        case None =>
          usernames
            .put(candidate, null.asInstanceOf[UserId])
            .as(candidate) // temp reserve
        case Some(_) => loop(i + 1)
      }
    }

    loop(0)
  }

  private def uFor(
                    id: UserId,
                    userName: String,
                    displayName: String,
                    at: Instant
                  ): User =
    User(
      id,
      userName,
      displayName,
      User.Status.Active,
      at,
      at
    )

  override def findById(id: UserId): IO[PersistenceError, Option[User]] =
    users.get(id).commit

  override def upsertActive(
                             id: UserId,
                             at: Instant
                           ): IO[PersistenceError, User] =
    (for {
      existing <- users.get(id)
      user <- existing match {
        case Some(u) =>
          val upd =
            if (u.status == User.Status.Active) u.copy(updatedAt = at)
            else u.copy(status = User.Status.Active, updatedAt = at)
          users.put(id, upd).as(upd)

        case None =>
          val (baseHandle, defaultDisplay) = UserLabels.from(id)
          val uniqueUsername = ensureUniqueUsername(baseHandle)
          uniqueUsername.flatMap {
            uname =>
              val u =
                uFor(id, uname, defaultDisplay, at) // construct your User
              users.put(id, u) *> usernames.put(uname, id) as u
          }
      }
    } yield user).commit

  override def touchLastLogin(
                               id: UserId,
                               at: Instant
                             ): IO[PersistenceError, Unit] = ???

}

object InMemoryUserRepo {
  val live: ULayer[UserReadPort & UserWritePort] =
    ZLayer.fromZIO {
      for {
        u  <- TMap.empty[UserId, User].commit
        ix <- TMap.empty[String, UserId].commit
      } yield new InMemoryUserRepo(u, ix)
    }
}
