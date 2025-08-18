import sbt.*

import scala.:+

object Dependencies {

  object V {
    val scala3           = "3.3.6"
    val jsoniter         = "2.37.1"
    val logback          = "1.5.6"
    val slf4j            = "2.0.13"
    val logbackEncoder   = "7.4"
    val tapir            = "1.11.41"
    val zio              = "2.1.20"
    val zioLogging       = "2.5.1"
    val zioConfig        = "4.0.4"
    val zioHttp          = "3.3.3"
    val zioIzumi         = "3.0.5"
    val sttpModel        = "1.7.16"
    val flyway           = "11.11.0"
    val postgres         = "42.7.3"
    val sttpShared       = "1.5.0"
    val sttpOpenapiModel = "0.11.10"
    val hikari           = "7.0.1"
    val embeddedPostgres = "2.0.4"
    val sttpClient       = "3.9.6"
    val webAuthn         = "2.7.0"
  }

  def prod(module: ModuleID, modules: ModuleID*): Seq[ModuleID] =
    module +: modules

  def testLibs(module: ModuleID, modules: ModuleID*): Seq[ModuleID] =
    (module +: modules).map(_ % Test)

  def provided(module: ModuleID, modules: ModuleID*): Seq[ModuleID] =
    (module +: modules).map(_ % Provided)

  def runtime(module: ModuleID, modules: ModuleID*): Seq[ModuleID] =
    (module +: modules).map(_ % Runtime)

  object Libs {
    val noSlf4j    = "org.slf4j" % "slf4j-nop"    % V.slf4j
    val zioTest    = "dev.zio"  %% "zio-test"     % V.zio
    val zioTestSbt = "dev.zio"  %% "zio-test-sbt" % V.zio
    val embeddedPostgres =
      "io.zonky.test" % "embedded-postgres" % V.embeddedPostgres

    val jsoniterCore =
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % V.jsoniter
    val jsoniterMacros =
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % V.jsoniter

    val logback = "ch.qos.logback" % "logback-classic" % V.logback
    val logbackEncoder =
      "net.logstash.logback" % "logstash-logback-encoder" % V.logbackEncoder

    val sttpOpenapiModel =
      "com.softwaremill.sttp.apispec" %% "openapi-model" % V.sttpOpenapiModel
    val sttpSharedCore = "com.softwaremill.sttp.shared" %% "core" % V.sttpShared
    val sttpSharedZio  = "com.softwaremill.sttp.shared" %% "zio"  % V.sttpShared

    val tapirCore = "com.softwaremill.sttp.tapir" %% "tapir-core" % V.tapir
    val tapirJsoniter =
      "com.softwaremill.sttp.tapir" %% "tapir-jsoniter-scala" % V.tapir
    val tapirRedocBundle =
      "com.softwaremill.sttp.tapir" %% "tapir-redoc-bundle" % V.tapir
    val tapirRedoc = "com.softwaremill.sttp.tapir" %% "tapir-redoc" % V.tapir
    val tapirOpenApiDocs =
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % V.tapir
    val tapirZio = "com.softwaremill.sttp.tapir" %% "tapir-zio" % V.tapir
    val tapirZioHttp =
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % V.tapir

    val hikari = "com.zaxxer"   % "HikariCP"    % V.hikari
    val flyway = "org.flywaydb" % "flyway-core" % V.flyway
    val flywayPostgres =
      "org.flywaydb" % "flyway-database-postgresql" % V.flyway
    val postgres  = "org.postgresql"               % "postgresql" % V.postgres
    val sttpModel = "com.softwaremill.sttp.model" %% "core"       % V.sttpModel

    val zio               = "dev.zio" %% "zio"                 % V.zio
    val zioConfig         = "dev.zio" %% "zio-config"          % V.zioConfig
    val zioConfigMagnolia = "dev.zio" %% "zio-config-magnolia" % V.zioConfig
    val zioConfigTypesafe = "dev.zio" %% "zio-config-typesafe" % V.zioConfig
    val zioHttp           = "dev.zio" %% "zio-http"            % V.zioHttp
    val zioIzumi          = "dev.zio" %% "izumi-reflect"       % V.zioIzumi
    val zioLogging        = "dev.zio" %% "zio-logging"         % V.zioLogging
    val zioLoggingSlf4j2  = "dev.zio" %% "zio-logging-slf4j2"  % V.zioLogging
    val zioStreams        = "dev.zio" %% "zio-streams"         % V.zio
    val zioStacktracer    = "dev.zio" %% "zio-stacktracer"     % V.zio

    val tapirSttpClient =
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % V.tapir
    val sttpClientCore =
      "com.softwaremill.sttp.client3" %% "core" % V.sttpClient
    val webAuthn = "com.yubico" % "webauthn-server-core" % V.webAuthn
  }
}
