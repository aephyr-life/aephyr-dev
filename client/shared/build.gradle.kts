import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    // iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // XCFramework output
    val xcframework = XCFramework("AephyrShared")
    targets.withType<KotlinNativeTarget> {
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

                implementation(libs.ktor.client.logging)

                // Kotlinx serialization
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                // Ktor core + JSON
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                // Ktor logging (so iOS gives you concrete errors instead of nw_connection spam)
                implementation(libs.ktor.client.logging)
            }
        }
        val commonTest by getting

        // Shared iOS source set (no files required; we just attach deps here)
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                // Crucial: Darwin engine so HttpClient() uses NSURLSession on iOS
                implementation(libs.ktor.client.darwin)
            }
        }

        // Make all iOS targets depend on iosMain
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}

// Convenience task for Xcode builds
tasks.register("packForXcode") {
    group = "build"
    dependsOn("assembleAephyrSharedDebugXCFramework") // or assembleAephyrSharedReleaseXCFramework
    doLast {
        val dir = layout.buildDirectory.dir("XCFrameworks/Debug").get().asFile
        val xc = dir.resolve("AephyrShared.xcframework")
        println("XCFramework (Debug): ${xc.absolutePath}")
    }
}
