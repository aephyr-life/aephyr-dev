//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.shared.config

import java.time.Duration
import zio.*
import zio.config.ConfigOps
import zio.config.magnolia.*
import zio.config.typesafe.*
import aephyr.kernel.StringOps.*


final case class HttpCfg(host: String, port: Int)

final case class DbPoolCfg(maxSize: Int, queueSize: Int)
final case class DbCfg(url: String, user: String, password: String, pool: DbPoolCfg)

final case class AuthCfg(webauthn: WebAuthnCfg)

final case class WebAuthnCfg(
                              rpId: String,
                              rpName: String,
                              origins: Set[String],
                              txTtl: Duration)

final case class LoggingCfg(format: String, level: String)

final case class AasaCfg(teamId: String, bundleId: String)

final case class AppConfig(
                            http: HttpCfg,
                            db: DbCfg,
                            auth: AuthCfg,
                            logging: LoggingCfg,
                            aasa: AasaCfg
                          )

object AppConfig {

  given Config[BaseUrl] = {
    Config.string
      .mapOrFail(s => BaseUrl.parse(s).left.map(
        t => Config.Error.InvalidData(Chunk.empty, t))
      )
  }

  private val desc = deriveConfig[AppConfig]
    .nested("app")
    .mapKey(_.camelToKebab)

  val layer: ZLayer[Any, Throwable, AppConfig] = ZLayer.fromZIO {
    TypesafeConfigProvider
      .fromResourcePath()
      .load(desc)
      .mapError(new RuntimeException(_))
  }

  val layerWithEnvVars: ZLayer[Any, Throwable, AppConfig] = {
    ZLayer.fromZIO {
      TypesafeConfigProvider
        .fromResourcePath()
        .load(desc)
        .mapError(e => new RuntimeException(e.toString))
    }
  }

  // optional convenience sub-layers
  val http: ZLayer[AppConfig, Nothing, HttpCfg] =
    ZLayer.fromFunction((c: AppConfig) => c.http)

  val db: ZLayer[AppConfig, Nothing, DbCfg] =
    ZLayer.fromFunction((c: AppConfig) => c.db)

  val auth: ZLayer[AppConfig, Nothing, AuthCfg] =
    ZLayer.fromFunction((c: AppConfig) => c.auth)

  val logging: ZLayer[AppConfig, Nothing, LoggingCfg] =
    ZLayer.fromFunction((c: AppConfig) => c.logging)
}
