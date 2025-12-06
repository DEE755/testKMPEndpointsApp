// composeApp/build.gradle.kts
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    // Targets
    androidTarget()
    jvmToolchain(17)
    jvm()
    val iosArm64 = iosArm64()
    val iosSimArm64 = iosSimulatorArm64()

    // CocoaPods (GoogleSignIn iOS) NOT SUPPORTED YET BY XCODE 26 AND CANT USE LESS THAN 26 SO USING WEBVIEW FOR IOS INSTEAD
    /*cocoapods {
        summary = "GSignIn"
        homepage = "https://example.com"
        version = "1.0.0"
        ios.deploymentTarget = "14.0"

        // pointer vers le Podfile de votre app iOS (ajustez le chemin si nécessaire)
        podfile = project.file("../iosApp/Podfile")

        // déclarer la dépendance CocoaPod iOS
        pod("GoogleSignIn", "~> 6.0")

        framework {
            baseName = "composeApp"
        }
    }*/

    // Source sets
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("io.ktor:ktor-client-logging:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("org.jetbrains.skiko:skiko:0.7.93")

                implementation("media.kamel:kamel-image:0.9.5")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                implementation("io.ktor:ktor-client-okhttp:2.3.12")
                implementation("io.ktor:ktor-client-logging:2.3.12")

                implementation("com.google.android.gms:play-services-auth:21.2.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
                implementation("androidx.activity:activity-ktx:1.9.2")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)

                implementation("io.ktor:ktor-client-cio:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.12")
                implementation("io.ktor:ktor-client-logging:2.3.12")
            }
        }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
        val commonTest by getting {
            dependencies { implementation(libs.kotlin.test) }
        }
    }
}

android {
    namespace = "com.example.demokmpinterfacetestingapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.demokmpinterfacetestingapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures { compose = true }

    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildTypes { getByName("release") { isMinifyEnabled = false } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    // Note: Play Services Auth est déjà déclaré dans androidMain
}

compose.desktop {
    application {
        mainClass = "com.example.demokmpinterfacetestingapp.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.demokmpinterfacetestingapp"
            packageVersion = "1.0.0"
        }
    }
}
