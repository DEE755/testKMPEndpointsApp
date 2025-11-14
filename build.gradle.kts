import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.jvm.java

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}

tasks.register("printKmpTargets") {
    group = "help"
    description = "Affiche les targets KMP et leurs compilations"
    doLast {
        val kmp = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        println("KMP targets:")
        kmp.targets.sortedBy { it.name }.forEach { t ->
            val comps = t.compilations.map { it.name }.sorted()
            println(" - ${t.name} -> ${comps.joinToString(", ")}")
        }
    }
}
