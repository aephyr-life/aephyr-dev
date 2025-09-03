package aephyr.adapters.security.webauthn.memory

import zio.*
import aephyr.auth.ports.UserHandleRepo
import aephyr.auth.domain.UserHandle
import aephyr.kernel.id.UserId

final case class InMemoryUserHandleRepo(store: Ref[Map[UserId, UserHandle]]) extends UserHandleRepo {

  def get(userId: UserId): Task[Option[UserHandle]] =
    store.get.map(_.get(userId))

  def put(userId: UserId, h: UserHandle): Task[Unit] =
    store.update(_ + (userId -> h)).unit

  def findByHandle(userHandle: UserHandle): Task[Option[UserId]] =
    store.get.map(_.collectFirst {
      case (uid, h) if h == userHandle => uid
    })

  def usernameFor(userId: UserId): Task[Option[String]] =
    ZIO.succeed(Some(userId.toString))
}
object InMemoryUserHandleRepo {
  val live: ZLayer[Any, Nothing, UserHandleRepo] =
    ZLayer.fromZIO(Ref.make(Map.empty[UserId, UserHandle]).map(InMemoryUserHandleRepo.apply))
}
