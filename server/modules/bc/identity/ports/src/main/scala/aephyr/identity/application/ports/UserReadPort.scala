package aephyr.identity.application.ports

import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import zio._

trait UserReadPort:

  def findById(id: User.Id): IO[PersistenceError, Option[User]]

  def findByEmail(email: User.EmailAddress): IO[PersistenceError, Option[User]]
