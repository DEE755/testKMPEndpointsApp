// settings.gradle.kts
rootProject.name = "demokmpinterfacetestingapp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
    }
    plugins {
        // ✅ Align versions
        id("com.android.application") version "8.8.0"
        id("org.jetbrains.kotlin.multiplatform") version "2.1.21"
        id("org.jetbrains.compose") version "1.8.0"

    }

    dependencyResolutionManagement {
        repositories {
            google {
                mavenContent {
                    includeGroupAndSubgroups("androidx")
                    includeGroupAndSubgroups("com.android")
                    includeGroupAndSubgroups("com.google")
                }
            }
            mavenCentral()
        }
    }

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }

    include(":composeApp")

}