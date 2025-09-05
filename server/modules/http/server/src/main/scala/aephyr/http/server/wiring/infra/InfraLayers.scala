package aephyr.http.server.wiring.infra

import aephyr.adapters.db.{DataSourceLayer, UserReadRepository, UserWriteRepository}
import aephyr.shared.config.AppConfig
import zio.*

object InfraLayers {

  type Env =
    AppConfig &
      UserReadRepository &
      UserWriteRepository
  
  val live: ZLayer[aephyr.shared.config.DbCfg, Throwable, Env] =
    (AppConfig.layer ++ DataSourceLayer.live) >>>
      (UserReadRepository.layer ++ UserWriteRepository.layer)
}
