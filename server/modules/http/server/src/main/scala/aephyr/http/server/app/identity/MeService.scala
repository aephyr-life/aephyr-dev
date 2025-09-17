package aephyr.http.server.app.identity

import aephyr.api.v0.identity.dto.{ Me, MeError }
import aephyr.identity.application.ports.UserReadPort
import aephyr.security.Principal
import zio.*

trait MeService {
  def me(principal: Principal): IO[MeError, Me]
}

object MeService {
  val live: URLayer[UserReadPort, MeService] =
    ZLayer.fromFunction(MeServiceLive.apply)
}

final class MeServiceLive(
  users: UserReadPort
) extends MeService:

  def me(principal: Principal): IO[MeError, Me] =
    for {
      user <- users
        .findById(principal.userId)
        .mapError(MeError.fromPersistence)
        .someOrFail(MeError.UserNotFound)
    } yield Me(
      id = user.id.asString,
      username = user.userName,
      displayName = user.displayName
    )
