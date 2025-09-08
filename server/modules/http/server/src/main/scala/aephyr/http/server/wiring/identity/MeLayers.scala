package aephyr.http.server.wiring.identity

import zio.*
import aephyr.api.shared.AuthenticationContext
import aephyr.api.v0.identity.dto.{Me, MeError}
import aephyr.http.server.app.identity.MeService
// import your repos as needed
// import aephyr.adapters.db.UserReadRepository

object MeLayers {

  // If you build from repos in InfraLayers, keep the required input here:
  // type In = UserReadRepository  // example
  // val dev: ZLayer[In, Nothing, MeService] = ...

  // Minimal stub/dev impl (accept any ctx) — replace with real logic using repos
  val dev: ULayer[MeService] =
    ZLayer.succeed(new MeService {
      override def me(ctx: AuthenticationContext): IO[MeError, Me] = {
        // Example: extract subject/userId from token if you have it
        val userId = ctx.bearer.value   // or parse JWT, etc.
        // TODO: fetch from repo using userId
        ZIO.succeed(Me(
          "id", "name", "display"
        ))
      }
    })
}
