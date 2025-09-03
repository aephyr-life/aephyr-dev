package aephyr.http.server.service.me

import zio.*
import aephyr.http.apis.endpoints.v0.me.Me
import aephyr.http.apis.endpoints.v0.me.MeError
import aephyr.http.server.auth.AuthExtractor
import aephyr.identity.application.ports.UserReadPort

trait MeService {
  def me: IO[MeError, Me]
}

object MeService {
  val live: URLayer[AuthExtractor & UserReadPort, MeService] =
    ZLayer.fromFunction(MeServiceLive.apply)
}

final class MeServiceLive(
                           extractor: AuthExtractor,
                           users:     UserReadPort
                         ) extends MeService:

  def me: IO[MeError, Me] =
    for {
      maybeId <- extractor.extract.mapError(_ => MeError.NotAuthenticated)
      userId  <- ZIO.fromOption(maybeId).orElseFail(MeError.NotAuthenticated)
      user    <- users.findById(userId)
        .mapError(MeError.fromPersistence)
        .someOrFail(MeError.UserNotFound)
    } yield Me(
      id          = user.id.asString,      // or keep it opaque in DTO if you prefer
      username    = user.userName,
      displayName = user.displayName
    )
