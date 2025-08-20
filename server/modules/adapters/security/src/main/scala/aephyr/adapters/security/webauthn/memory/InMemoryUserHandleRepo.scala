package aephyr.adapters.security.webauthn.memory

import zio.*
import aephyr.auth.ports.UserHandleRepo
import aephyr.auth.domain.UserHandle
import aephyr.identity.domain.User

final case class InMemoryUserHandleRepo(store: Ref[Map[User.Id, UserHandle]]) extends UserHandleRepo {
  def get(userId: User.Id)                      = store.get.map(_.get(userId))
  def put(userId: User.Id, h: UserHandle): Task[Unit] = store.update(_ + (userId -> h)).unit
}
object InMemoryUserHandleRepo {
  val live: ZLayer[Any, Nothing, UserHandleRepo] =
    ZLayer.fromZIO(Ref.make(Map.empty[User.Id, UserHandle]).map(InMemoryUserHandleRepo.apply))
}
