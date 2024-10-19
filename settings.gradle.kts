pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("com.google.devtools.ksp").version("1.9.21-1.0.16")
        id("androidx.room").version("2.7.0-alpha02")
    }
}

rootProject.name = "DeepCodeStudio"
include("database")
include("settings")
include("common")
include("terminal")
include("editor")
