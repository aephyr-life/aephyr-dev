package aephyr.adapters.db

import com.zaxxer.hikari.HikariDataSource
import zio.{ZIO, ZLayer}

import javax.sql.DataSource

object DataSourceLayer {
  val live: ZLayer[Any, Throwable, DataSource] =
    ZLayer.fromZIO {
      ZIO.attempt {
        val ds = new HikariDataSource()
        ds.setJdbcUrl(sys.env("DB_URL"))
        ds.setUsername(sys.env("DB_USER"))
        ds.setPassword(sys.env("DB_PASSWORD"))
        ds.setMaximumPoolSize(10)
        ds
      }
    }.tapError(e => ZIO.logError(s"❌ Failed to init DataSource: ${e.getMessage}"))
}
