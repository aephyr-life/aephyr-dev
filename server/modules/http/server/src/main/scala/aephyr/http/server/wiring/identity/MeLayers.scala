package aephyr.http.server.wiring.identity

import aephyr.api.v0.identity.dto.{Me, MeError}
import aephyr.http.server.app.identity.MeService
import zio.*

object MeLayers {
  type Env = MeService

  // Provide your real implementation here once you have it
  val dev: ZLayer[Any, Nothing, Env] =
    ZLayer.succeed(new MeService {
      def me(id: String): IO[MeError, Me] = ???
    })
}
