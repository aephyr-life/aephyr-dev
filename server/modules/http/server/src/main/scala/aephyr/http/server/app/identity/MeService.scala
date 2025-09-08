package aephyr.http.server.app.identity

import aephyr.api.v0.identity.dto.{Me, MeError}
import aephyr.identity.application.ports.UserReadPort
import zio.*

trait MeService {
  def me(id: String): IO[MeError, Me]
}

object MeService {
  val live: URLayer[UserReadPort, MeService] =
    ZLayer.fromFunction(MeServiceLive.apply)
}

final class MeServiceLive(
                           users:     UserReadPort
                         ) extends MeService:

  def me(bla: String): IO[MeError, Me] =
    for {
      maybeId <- ZIO.succeed(None) //extractor.extract.mapError(_ => MeError.NotAuthenticated)
      userId  <- ZIO.fromOption(maybeId).orElseFail(MeError.NotAuthenticated)
      user    <- users.findById(userId)
        .mapError(MeError.fromPersistence)
        .someOrFail(MeError.UserNotFound)
    } yield Me(
      id          = user.id.asString,
      username    = user.userName,
      displayName = user.displayName
    )
