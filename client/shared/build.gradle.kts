import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    jvmToolchain(17)

    applyDefaultHierarchyTemplate()

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }

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
            }
        }
        val commonTest by getting
        val iosMain by getting { dependencies { implementation(libs.ktor.client.darwin) } }
    }
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
