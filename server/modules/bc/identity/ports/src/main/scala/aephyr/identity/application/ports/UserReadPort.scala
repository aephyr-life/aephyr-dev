package aephyr.identity.application.ports

import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import zio._

import aephyr.kernel.id.UserId

trait UserReadPort:

  def findById(id: UserId): IO[PersistenceError, Option[User]]

  def findByEmail(email: User.EmailAddress): IO[PersistenceError, Option[User]]
