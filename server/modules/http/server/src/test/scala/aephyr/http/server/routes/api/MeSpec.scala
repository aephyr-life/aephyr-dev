package aephyr.http.server.routes.api

import zio.*
import zio.test.*
import zio.http.*
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import aephyr.http.server.routes.api.MeRoutes
import aephyr.auth.ports.*
import aephyr.http.server.auth.AuthExtractor
import aephyr.identity.application.ports.UserReadPort
import aephyr.identity.domain.User
import aephyr.http.apis.endpoints.v0.me.{Me, MeError}
import aephyr.http.server.service.me.MeService
import aephyr.kernel.id.UserId

import java.time.Instant

object MeSpec extends ZIOSpecDefault {

  private val authLayer: ULayer[AuthExtractor] =
    ZLayer.succeed(new AuthExtractor {
      def extract = ZIO.succeed(Some(UserId.random))
    })

  private def userLayer(me: Me): ULayer[UserReadPort] =
    ZLayer.succeed(new UserReadPort {
      def findById(id: UserId) =
        ZIO.succeed(Some(User(
          id,
          me.username,
          me.displayName,
          User.Status.Active,
          Instant.now.nn,
          Instant.now.nn
        )))
    })

  private def service(myself: Me): ULayer[MeService] =
    ZLayer.succeed(new MeService {
      override def me: IO[MeError, Me] = ZIO.succeed(myself)
    })

  val app: Routes[MeService, Response] =
    ZioHttpInterpreter().toHttp(MeRoutes.routes)

  def spec = suite("/api/me")(
    test("returns current user alice") {
      val me = Me("alices-id", "alice", "Alice") // TODO maybe use UserId
      val req = Request.get(URL.decode("/api/me").toOption.get)
      for {
        res    <- app.runZIO(req).provide(service(me), Scope.default)
        body   <- res.body.asString
      } yield assertTrue(res.status.isSuccess && body.contains("alice"))
    }
  )
}
