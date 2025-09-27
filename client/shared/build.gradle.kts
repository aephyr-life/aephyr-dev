import com.google.devtools.ksp.gradle.KspAATask
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kmp.nativecoroutines)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotest)
    alias(libs.plugins.test.logger)
}

dependencies {
    add("kspCommonMainMetadata", libs.kmp.nativecoroutines.ksp)   // <-- processor
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvmToolchain(21)

    applyDefaultHierarchyTemplate()

    jvm()

    // iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // XCFramework output
    val xcframework = XCFramework("AephyrShared")
    targets.withType<KotlinNativeTarget> {
        binaries.framework {
            baseName = "AephyrShared"
            binaryOption("bundleId", "life.aephyr.shared")
            isStatic = true
            xcframework.add(this)
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCRefinement")
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kmp.nativecoroutines.core)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.russhwolf.settings)
                implementation(libs.russhwolf.settings.coroutines)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.assert)
                implementation(libs.kotest.framework.engine)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.native.driver)
            }
        }
    }
}

sqldelight {
    databases {
        create("FoodDb") {
            packageName.set("aephyr.shared.foodlog.db")
            // schemaOutputDirectory, verifyMigrations… if you want
        }
    }
}

kover {
}

tasks.register("packForXcode") {
    group = "build"
    dependsOn("assembleAephyrSharedDebugXCFramework") // or Release
    doLast {
        val dir = layout.buildDirectory.dir("XCFrameworks/Debug").get().asFile
        val xc = dir.resolve("AephyrShared.xcframework")
        println("XCFramework (Debug): ${xc.absolutePath}")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    reports {
        junitXml.required.set(true)   // build/test-results/test/*.xml
        html.required.set(true)       // build/reports/tests/test/index.html
    }
}

// nice shortcut to generate HTML & XML
tasks.register("coverage") {
    dependsOn("koverHtmlReport", "koverXmlReport")
}
