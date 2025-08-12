package aephyr.adapters.db

import aephyr.shared.config.DbCfg
import com.zaxxer.hikari.HikariDataSource
import zio.{ ZIO, ZLayer }

object DataSourceLayer {

  val live: ZLayer[DbCfg, Throwable, HikariDataSource] = ZLayer.scoped {
    for {
      cfg <- ZIO.service[DbCfg]
      ds <- ZIO.acquireRelease {
        ZIO
          .attempt {
            val ds = new HikariDataSource()
            ds.setJdbcUrl(cfg.url)
            ds.setUsername(cfg.user)
            ds.setPassword(cfg.password)
            ds.setMaximumPoolSize(cfg.pool.maxSize)
            ds.setMinimumIdle(math.min(1, cfg.pool.maxSize))
            ds
          }
          .tapError(
            e => ZIO.logError(s"❌ Failed to init DataSource: ${e.getMessage}")
          )
      }(
        ds => ZIO.attempt(ds.close()).orDie
      )
    } yield ds
  }
}
