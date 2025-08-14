package aephyr.adapters.db

import zio._

final case class DbConfig(url: String, user: String, password: String)

object DbConfig:

  val live: ZLayer[Any, RuntimeException, DbConfig] =
    ZLayer.fromZIO {
      def req(name: String) =
        ZIO
          .fromOption(sys.env.get(name))
          .orElseFail(new RuntimeException(s"missing required env var: $name"))
      for
        url  <- req("APP_DB_URL")
        user <- req("APP_DB_USER")
        pass <- req("APP_DB_PASSWORD")
      yield DbConfig(url, user, pass)
    }
