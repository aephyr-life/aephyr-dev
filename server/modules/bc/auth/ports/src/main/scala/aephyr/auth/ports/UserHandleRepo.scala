package aephyr.auth.ports

import zio.*
import aephyr.auth.domain.UserHandle
import aephyr.kernel.id.UserId

trait UserHandleRepo {
  def get(userId: UserId): Task[Option[UserHandle]]
  def put(userId: UserId, handle: UserHandle): Task[Unit]
  def findByHandle(handle: UserHandle): Task[Option[UserId]]
  def usernameFor(userId: UserId): Task[Option[String]]
}
