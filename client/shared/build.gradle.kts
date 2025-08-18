import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    // iOS targets for now; add Android later with androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    val xcframework = XCFramework("AephyrShared")

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.framework {
            baseName = "AephyrShared"
            isStatic = true
            xcframework.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.ktor.client.core)
            }
        }
        val commonTest by getting

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}

// Convenience task for Xcode builds

tasks.register("packForXcode") {
    group = "build"
    dependsOn("assembleAephyrSharedDebugXCFramework") // or assembleReleaseXCFramework
    doLast {
        val dir = layout.buildDirectory.dir("XCFrameworks/Debug").get().asFile
        val xc = dir.resolve("AephyrShared.xcframework")
        println("XCFramework (Debug): ${xc.absolutePath}")
    }
}

