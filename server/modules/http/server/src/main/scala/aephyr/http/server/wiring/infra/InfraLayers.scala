package aephyr.http.server.wiring.infra

import aephyr.adapters.db.{DataSourceLayer, UserReadRepository, UserWriteRepository}
import aephyr.shared.config.AppConfig
import zio.*

object InfraLayers {
  
  val live: ZLayer[
    Any, 
    Throwable, 
    AppConfig & UserReadRepository & UserWriteRepository
  ] =
    AppConfig.layer ++
      DataSourceLayer.live ++
      UserReadRepository.layer ++
      UserWriteRepository.layer
}
