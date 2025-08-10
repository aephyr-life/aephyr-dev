package aephyr.identity.application.ports

import zio.*
import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError

trait UserReadPort:

  def findById(id: User.Id): IO[PersistenceError, Option[User]]

  def findByEmail(email: User.EmailAddress): IO[PersistenceError, Option[User]]
