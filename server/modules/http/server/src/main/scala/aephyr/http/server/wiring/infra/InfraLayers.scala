package aephyr.http.server.wiring.infra

import zio.*

import javax.sql.DataSource
import aephyr.shared.config.AppConfig
import aephyr.adapters.db.DataSourceLayer

import aephyr.identity.application.ports.{UserReadPort, UserWritePort}


object InfraLayers {
  // What the rest of the app should depend on:
  type Env = AppConfig & UserReadPort & UserWritePort

  // Build a JDBC DataSource from AppConfig.db
  private val dataSource: ZLayer[AppConfig, Throwable, DataSource] =
    AppConfig.db >>> DataSourceLayer.live

  // Widen adapters to their port types (adapters should implement the ports)
  private val readPort: ZLayer[DataSource, Nothing, UserReadPort] =
    InMemoryUserRepo.live
  private val writePort: ZLayer[DataSource, Nothing, UserWritePort] =
    InMemoryUserRepo.live

  private val repos
    : ZLayer[AppConfig, Throwable, UserReadPort & UserWritePort] =
    dataSource >>> (readPort ++ writePort)

  val live: ZLayer[AppConfig, Throwable, Env] =
    repos ++ ZLayer.service[AppConfig] // pass AppConfig through
}
