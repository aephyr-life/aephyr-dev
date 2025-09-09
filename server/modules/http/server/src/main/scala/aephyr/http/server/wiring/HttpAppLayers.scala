package aephyr.http.server.wiring

import aephyr.adapters.security.jwt.NimbusJwtVerifier
import aephyr.auth.ports.JwtVerifier
import aephyr.identity.application.ports.UserReadPort
import aephyr.http.server.app.identity.MeService
import aephyr.http.server.endpoint.HttpTypes
import aephyr.http.server.security.AuthService
import aephyr.http.server.wiring.infra.InfraLayers
import aephyr.http.server.wiring.identity.WebAuthnLayers
import aephyr.shared.config.{AasaCfg, AppConfig, JwtCfg}
import zio.*

object HttpAppLayers {
  type Env = HttpTypes.Env // PublicEnv & IdentityEnv & SecurityEnv

  // JwtCfg -> Nimbus config
  val nimbusCfgFromApp: ZLayer[JwtCfg, Nothing, NimbusJwtVerifier.Config] =
    ZLayer.fromFunction((j: JwtCfg) =>
      NimbusJwtVerifier.Config(j.issuer, j.audience, j.jwkSetJson)
    )

  // Build JwtVerifier from AppConfig + Clock
  val jwtVerifierLayer: ZLayer[AppConfig & Clock, Nothing, JwtVerifier] =
    (AppConfig.jwt >>> nimbusCfgFromApp) ++ ZLayer.service[Clock] >>> NimbusJwtVerifier.live

  // 🔧 Inputs for whole app: Infra needs AppConfig; security needs AppConfig & Clock; public needs AasaCfg
  private type In = AppConfig & AasaCfg & Clock

  // Infra is built from AppConfig (NOT DbCfg)
  private val infra: ZLayer[AppConfig, Throwable, InfraLayers.Env] =
    InfraLayers.live

  // Identity env: MeService from infra (provides UserReadPort), plus WebAuthn (needs AppConfig & Clock)
  private val identityLayers: ZLayer[In, Throwable, HttpTypes.IdentityEnv] =
    (infra >>> MeService.live) ++ WebAuthnLayers.dev

  // Public env: just expose AASA config
  private val publicLayers: ZLayer[In, Nothing, HttpTypes.PublicEnv] =
    ZLayer.service[AasaCfg]

  // Security env: AuthService needs JwtVerifier & UserReadPort
  private val securityLayers: ZLayer[In, Throwable, HttpTypes.SecurityEnv] =
    ((jwtVerifierLayer ++ (infra >>> ZLayer.service[UserReadPort])) >>> AuthService.live)

  // Final assembly
  val dev: ZLayer[In, Throwable, Env] =
    identityLayers ++ publicLayers ++ securityLayers
}
