package aephyr.http.server.app.identity

import zio.test.*
import zio.test.Assertion.*
import aephyr.api.v0.identity.dto.{ Me, MeError }
import aephyr.http.server.app.identity.MeServiceSpec.test
import aephyr.identity.application.ports.UserReadPort

import scala.language.unsafeNulls
import aephyr.identity.domain.User
import aephyr.kernel.PersistenceError
import aephyr.kernel.id.UserId
import aephyr.security.Principal
import zio.{ IO, ULayer, ZIO, ZLayer }

import java.time.Instant

object MeServiceSpec extends ZIOSpecDefault {

  private val u1: UserId = UserId.random
  private val u2: UserId = UserId.random

  private val users = Map(
    u1 -> User(
      u1,
      "Ada Lovelace",
      "Ada",
      User.Status.Active,
      Instant.now(),
      Instant.now()
    ),
    u2 -> User(
      u2,
      "Alan Turing",
      "Alan",
      User.Status.Active,
      Instant.now(),
      Instant.now()
    )
  )

  private val fakeUserRepo: ULayer[UserReadPort] =
    ZLayer.succeed(new UserReadPort {
      override def findById(id: UserId): IO[PersistenceError, Option[User]] =
        ZIO.succeed(users.get(id))
    })

  private val failingRepo: ULayer[UserReadPort] =
    ZLayer.succeed(new UserReadPort {
      override def findById(id: UserId): IO[PersistenceError, Option[User]] =
        ZIO.fail(PersistenceError.Timeout("timeout"))
    })

  private val layer = fakeUserRepo >>> MeService.live

  def spec =
    suite("MeService")(
      // TODO test PersistenceError
      test("returns user info when user exists") {
        for {
          svc <- ZIO.service[MeService]
          me <- svc.me(Principal(u1))
        } yield assert(me)(equalTo(Me(u1.asString, "Ada Lovelace", "Ada")))
      }.provideLayer(layer),
      test("fails with NotFound when user does not exist") {
        for {
          svc <- ZIO.service[MeService]
          exit <- svc.me(Principal(UserId.random)).exit
        } yield assert(exit)(fails(equalTo(MeError.UserNotFound)))
      }.provideLayer(layer),
      test("fails with a persistence error") {
        for {
          svc <- ZIO.service[MeService]
          exit <- svc.me(Principal(UserId.random)).exit
        } yield assert(exit)(
          fails(
            equalTo(
              MeError.RepoFailure(
                "aephyr.kernel.PersistenceError$Timeout: timeout"
              )
            )
          )
        )
      }.provideLayer(failingRepo >>> MeService.live)
    )
}
