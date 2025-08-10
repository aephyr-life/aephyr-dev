package aephyr.adapters.db

import zio.*
import javax.sql.DataSource
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres

object TestDsLayer {

  val embedded: ZLayer[Any, Throwable, DataSource] =
    ZLayer.scoped {
      ZIO.acquireRelease(
        ZIO.attempt(EmbeddedPostgres.start())          // starts PG on a random free port
      )(ep => ZIO.attempt(ep.close()).orDie).map { ep =>
        // You can also use ep.getPostgresDatabase (a DataSource),
        // but Hikari is nice to keep behavior close to prod:
        val hc = new HikariConfig()
        hc.setJdbcUrl(ep.getJdbcUrl("postgres", "postgres")) // db/user "postgres"
        hc.setUsername("postgres")
        hc.setPassword("")
        hc.setMaximumPoolSize(4)
        new HikariDataSource(hc): DataSource
      }
    }
}