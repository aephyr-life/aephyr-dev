import Dependencies.*

Global / concurrentRestrictions := Seq()

ThisBuild / organization  := "life.aephyr"
ThisBuild / scalaVersion  := V.scala3
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / usePipelining     := false
ThisBuild / exportPipelining  := false

//ThisBuild / conflictManager := ConflictManager.strict
ThisBuild / updateOptions := updateOptions.value.withLatestSnapshots(false)

// off by default
ThisBuild / semanticdbEnabled := false
// ThisBuild / semanticdbVersion := scalafixSemanticDb.revision

// enable only on hot modules (edit this list)
httpServer / semanticdbEnabled       := false
authDomain / semanticdbEnabled      := false
authApplication / semanticdbEnabled := false
identityDomain / semanticdbEnabled  := false

ThisBuild / cancelable.withRank(KeyRanks.Invisible) := true

// ThisBuild / turbo := true                         // faster reload / project switches
// ThisBuild / parallelExecution := true             // compile/test in parallel across modules
Test / fork := false                              // ZIO Test runs fine in-process and is faster

// fewer eviction recalculations during reload
ThisBuild / evictionErrorLevel := Level.Info
// print timings to spot bottlenecks
ThisBuild / javaOptions += "-Dsbt.task.timings=true"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Wunused:all",
  "-Wvalue-discard",
  "-Wnonunit-statement",
  "-explain",
  "-explain-types",
  "-Yexplicit-nulls",
  "-language:strictEquality"
)

ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

ThisBuild / dependencyOverrides := Seq(
  Libs.zio,
  Libs.zioStreams,
  Libs.zioIzumi,
  Libs.zioLogging,
  Libs.zioLoggingSlf4j2,
  Libs.sttpModel,
  Libs.sttpSharedCore,
  Libs.sttpSharedZio
)

ThisBuild / libraryDependencies ++= testLibs(
  Libs.noSlf4j,
  Libs.zio,
  Libs.zioTest,
  Libs.zioTestSbt,
  Libs.embeddedPostgres,
  Libs.flyway,
  Libs.flywayPostgres
)

// Only for development builds (not in production CI)
//ThisBuild / scalacOptions ++= Seq(
//  "-Xfatal-warnings" // Fail compilation on warnings
//)

httpServer / fork         := true
httpServer / connectInput := true

def mod(p: String, n: String) = Project.apply(n, file(s"modules/$p"))

lazy val root = (project in file("."))
  .aggregate(
    sharedKernel,
    authDomain,
    authApplication,
    identityDomain,
    identityApplication,
    diaryDomain,
    diaryApplication,
    adaptersDb,
    adaptersMessaging,
    adaptersImport,
    httpServer,
    dbMigrations
  )
  .settings(publish / skip := true)

lazy val sharedKernel = mod("shared/kernel", "shared-kernel")

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
// -------- BC: auth
lazy val authDomain = mod("bc/auth/domain", "auth-domain")
  .dependsOn(sharedKernel, identityDomain % "compile->compile")

lazy val authApplication =
  mod("bc/auth/application", "auth-application")
    .dependsOn(authDomain, authPorts, sharedApplication)
    .settings(
      libraryDependencies ++= Seq(
        Libs.zio,
        Libs.zioLogging,
        Libs.zioStacktracer
      )
    )

lazy val authPorts = mod("bc/auth/ports", "auth-ports")
  .dependsOn(sharedKernel, authDomain, identityDomain)
  .settings(
    libraryDependencies ++= Seq(
      Libs.zio,
      Libs.zioLogging,
      Libs.zioStacktracer
    )
  )

// -------- BC: identity
lazy val identityDomain = mod("bc/identity/domain", "identity-domain")
  .dependsOn(sharedKernel)

lazy val identityApplication =
  mod("bc/identity/application", "identity-application")
    .dependsOn(identityDomain, identityPorts, sharedApplication)
    .settings(
      libraryDependencies ++= Seq(
        Libs.zio,
        Libs.zioLogging,
        Libs.zioStacktracer
      )
    )

lazy val identityPorts =
  mod("bc/identity/ports", "identity-ports")
    .dependsOn(identityDomain)
    .settings(
      libraryDependencies ++= Seq(
        Libs.zio,
        Libs.zioLogging,
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
  .dependsOn(identityApplication, diaryApplication, authPorts, sharedKernel)
  .settings(
    Test / unmanagedResourceDirectories += (dbMigrations / Compile / resourceDirectory).value,
    libraryDependencies ++= prod(
      Libs.zio,
      Libs.zioStacktracer,
      Libs.hikari,
      Libs.postgres
    )
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
  .dependsOn(sharedApplication, sharedKernel, identityPorts, authPorts)
  .settings(
    libraryDependencies ++= prod(
      Libs.zio,
      Libs.webAuthn
    )
  )

// -------- APIs
lazy val httpApis = mod("http/apis", "http-apis")
  .settings(
    libraryDependencies ++= Seq(
      Libs.sttpModel,
      Libs.tapirCore,
      Libs.tapirJsoniter,
      Libs.jsoniterCore
    ) ++ provided(Libs.jsoniterMacros)
  )

lazy val httpServer = mod("http/server", "http-server")
  .dependsOn(httpApis, sharedKernel, adaptersSecurity, adaptersDb, authPorts)
  .settings(
    libraryDependencies ++= Seq(
      Libs.tapirCore,
      Libs.tapirRedocBundle,
      Libs.tapirRedoc,
      Libs.tapirZio,
      Libs.tapirJsonZio,
      Libs.tapirOpenApiDocs,
      Libs.tapirZioHttp,
      Libs.tapirSttpClient,
      Libs.sttpClientCore,
      Libs.webAuthn,
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
    Compile / mainClass := Some("aephyr.http.server.HttpServer"),
    run / javaOptions ++= Seq("-Dconfig.resource=application-dev.conf")
  )

lazy val dbMigrations = mod("db-migrations", "db-migrations")
  .settings(
    libraryDependencies ++= prod(
      Libs.postgres,
      Libs.flyway
    ) ++ runtime(
      Libs.flywayPostgres
    )
  )
