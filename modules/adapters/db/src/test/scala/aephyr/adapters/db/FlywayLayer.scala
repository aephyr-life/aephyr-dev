package aephyr.adapters.db

import zio.*
import javax.sql.DataSource

object FlywayLayer {
  val migrate: ZLayer[DataSource, Throwable, DataSource] =
    ZLayer.fromZIO {
      for {
        ds <- ZIO.service[DataSource]
        _  <- ZIO.attempt {
          org.flywaydb.core.Flyway.configure()
            .dataSource(ds)
            .schemas("read","tech","events")
            .locations("classpath:db/migration")
            .loggers("slf4j")
            .load()
            .migrate()
        }.unit
      } yield ds
    }
}
