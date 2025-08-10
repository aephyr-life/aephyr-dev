package aephyr.db.migration

import org.flywaydb.core.Flyway

object Migration:
  @main def migrate(): Unit =
    val url = sys.env("FLYWAY_URL")
    val user = sys.env("FLYWAY_USER")
    val pass = sys.env("FLYWAY_PASSWORD")
    Flyway.configure()
      .dataSource(url, user, pass)
      .schemas("events", "read", "tech")
      .locations("classpath:db/migration")
      .baselineOnMigrate(true)
      .load()
      .migrate()
