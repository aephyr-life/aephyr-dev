package aephyr.adapters.db

import scala.language.unsafeNulls

import java.util.UUID
import javax.sql.DataSource

import aephyr.identity.application.ports.UserReadPort
import aephyr.identity.domain.User
import zio._
import zio.test._

object UserReadRepositorySpec extends ZIOSpecDefault {

  // Small seed helper
  def seedUser(ds: DataSource, id: UUID, email: String): Task[Unit] =
    JdbcMini
      .withConnection(ds) {
        JdbcMini
          .execute(
            "INSERT INTO read.users_read(id, email_norm, status) VALUES (?::uuid, ?, 'active') ON CONFLICT DO NOTHING",
            Seq(Option(id), Option(email).map(_.trim.toLowerCase))
          )
          .unit
      }
      .mapError(
        e => new RuntimeException(e.toString)
      )

  private val dsEnv = TestDsLayer.embedded >>> FlywayLayer.migrate
  private val repoL = dsEnv >>> UserReadRepository.layer

  override def spec =
    suite("UserReadRepository")(
      test("findById returns the seeded user") {
        for {
          ds <- ZIO.service[DataSource]
          _ <- seedUser(
            ds,
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "alice@example.com"
          )
          repo <- ZIO.service[UserReadPort] // provided by UserRepository.layer
          got <- repo
            .findById(
              User.Id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
            )
            .mapError(
              e => new RuntimeException(e.toString)
            )
        } yield assertTrue(
          got.exists(_.emailNorm == User.EmailAddress("alice@example.com"))
        )
      }
    ).provideShared(
      dsEnv,
      repoL
    )
}
