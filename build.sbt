import Dependencies._

ThisBuild / scalaVersion := V.scala3
ThisBuild / organization := "life.aephyr"
ThisBuild / cancelable.withRank(KeyRanks.Invisible) := true

apiServer / fork := true
apiServer / connectInput := true

def mod(p: String, n: String) = Project.apply(n, file(s"modules/$p"))

lazy val root = (project in file("."))
  .aggregate(
    sharedKernel,
    identityDomain, identityApplication,
    diaryDomain, diaryApplication,
    adaptersDb, adaptersMessaging, adaptersImport,
    commandApi, queryApi, apiServer, dbMigrations
  )
  .settings(publish / skip := true)

lazy val sharedKernel = mod("shared/kernel", "shared-kernel")
  .settings(
    libraryDependencies ++= testLibs(
      Libs.zio,
      Libs.zioTest,
      Libs.zioTestSbt
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val sharedApplication = mod("shared/application", "shared-application")
  .dependsOn(sharedKernel)
  .settings(
    libraryDependencies ++= Seq(
      Libs.zio,
      Libs.zioConfig,
      Libs.zioConfigTypesafe,
      Libs.zioConfigMagnolia
    )
  )

// -------- BC: identity
lazy val identityDomain = mod("bc/identity/domain", "identity-domain")
  .dependsOn(sharedKernel)

lazy val identityApplication = mod("bc/identity/application", "identity-application")
  .dependsOn(identityDomain, sharedApplication)
  .settings(
    libraryDependencies ++= Seq(
      Libs.zio,
      Libs.zioStacktracer
    )
  )

// -------- BC: diary
lazy val diaryDomain = mod("bc/diary/domain", "diary-domain")
  .dependsOn(sharedKernel)

lazy val diaryApplication = mod("bc/diary/application", "diary-application")
  .dependsOn(diaryDomain, identityDomain)

// -------- Adapters (implement Ports from *Application)
lazy val adaptersDb = mod("adapters/db", "adapters-db")
  .dependsOn(identityApplication, diaryApplication, sharedKernel)
  .settings(
    Test / unmanagedResourceDirectories += (dbMigrations / Compile / resourceDirectory).value,
    libraryDependencies ++= prod(
      Libs.zio,
      Libs.zioStacktracer,
      Libs.hikari,
      Libs.postgres,
    ) ++ testLibs(
      Libs.noSlf4j,
      Libs.zioTest,
      Libs.zioTestSbt,
      Libs.embeddedPostgres,
      Libs.flyway,
      Libs.flywayPostgres
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val adaptersMessaging = mod("adapters/messaging", "adapters-messaging")
  .dependsOn(identityApplication, diaryApplication, sharedKernel)
  .settings(
    libraryDependencies ++= prod(
      Libs.zio,
      Libs.zioStacktracer
    )
  )

lazy val adaptersImport = mod("adapters/import", "adapters-import")
  .dependsOn(sharedKernel)

lazy val adaptersSecurity = mod("adapters/security", "adapters-security")
  .dependsOn(sharedApplication, sharedKernel)
  .settings(
    libraryDependencies ++= prod(
      Libs.zio
    )
  )

// -------- APIs
lazy val apisShared = mod("apis/shared", "apis-shared")
  .dependsOn(sharedKernel, identityDomain)
  .settings(
    libraryDependencies ++= prod(
      Libs.sttpModel,
      Libs.jsoniterCore
    ) ++ provided(
      Libs.jsoniterMacros
    )
  )

lazy val commandApi = mod("apis/command", "command-api")
  .dependsOn(identityApplication, diaryApplication, sharedApplication, sharedKernel, adaptersDb, adaptersMessaging, apisShared)
  .settings(
    libraryDependencies ++= Seq(
      Libs.jsoniterCore,
      Libs.tapirCore,
      Libs.tapirJsoniter
    ) ++ provided(
      Libs.jsoniterMacros
    )
  )

lazy val queryApi = mod("apis/query", "query-api")
  .dependsOn(sharedKernel, apisShared)
  .settings(
    libraryDependencies ++= Seq(
      Libs.jsoniterCore,
      Libs.tapirCore,
      Libs.tapirJsoniter,
      Libs.sttpModel
    ) ++ provided(
      Libs.jsoniterMacros
    )
  )

lazy val apiServer = mod("apis/server", "api-server")
  .dependsOn(apisShared, sharedKernel, commandApi, adaptersSecurity, queryApi)
  .settings(
    libraryDependencies ++= Seq(
      Libs.tapirCore,
      Libs.tapirRedocBundle,
      Libs.tapirRedoc,
      Libs.tapirZio,
      Libs.tapirOpenApiDocs,
      Libs.tapirZioHttp,
      Libs.zio,
      Libs.zioStacktracer,
      Libs.zioHttp,
      Libs.zioLogging,
      Libs.zioLoggingSlf4j2,
      Libs.zioIzumi,
      Libs.sttpOpenapiModel,
      Libs.sttpSharedCore,
      Libs.sttpSharedZio
    ) ++ runtime(
      Libs.logback,
      Libs.logbackEncoder
    ),
    Compile / mainClass := Some("aephyr.adapters.api.server.ApiServerMain")
  )

lazy val dbMigrations  = mod("db-migrations", "db-migrations")
  .settings(
    libraryDependencies ++= prod(
      Libs.postgres,
      Libs.flyway
    ) ++ runtime(
      Libs.flywayPostgres
    )
  )
