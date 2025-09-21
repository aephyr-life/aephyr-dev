import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    applyDefaultHierarchyTemplate()

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
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
+                implementation("com.rickclephas.kmp:kmp-nativecoroutines-core:1.0.0-ALPHA-36")
            }
        }
        val commonTest by getting

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

 // Optional: disable main-thread-only enforcement for ObjC suspend exports
 targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().all {
     binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().all {
         binaryOption("objcExportSuspendFunctionLaunchThreadRestriction", "none")
     }
 }

// Convenience task for Xcode builds
tasks.register("packForXcode") {
    group = "build"
    dependsOn("assembleAephyrSharedDebugXCFramework") // or Release
    doLast {
        val dir = layout.buildDirectory.dir("XCFrameworks/Debug").get().asFile
        val xc = dir.resolve("AephyrShared.xcframework")
        println("XCFramework (Debug): ${xc.absolutePath}")
    }
}
