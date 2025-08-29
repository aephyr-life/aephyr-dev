package aephyr.auth.ports

import zio.*
import aephyr.auth.domain.UserHandle
import aephyr.identity.domain.User

trait UserHandleRepo {
  def get(userId: User.Id): Task[Option[UserHandle]]
  def put(userId: User.Id, handle: UserHandle): Task[Unit]
  def findByHandle(handle: UserHandle): Task[Option[User.Id]]
  def usernameFor(userId: User.Id): Task[Option[String]]
}
