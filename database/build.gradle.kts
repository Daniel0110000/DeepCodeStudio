plugins {
    kotlin("jvm")
    id("app.cash.sqldelight") version "2.0.0"
    id("com.google.devtools.ksp")
    id("androidx.room")
}

group = "dev.daniel"
version = "1.0.0"

repositories {
    mavenCentral()
    google()
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("app.deepCodeStudio.database")
        }
    }
}

room {
    schemaDirectory("${projectDir}/schemas")
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    // Koin
    implementation("io.insert-koin:koin-core:3.5.0")

    // Room
    implementation("androidx.room:room-runtime:2.7.0-alpha10")
    implementation("androidx.sqlite:sqlite-bundled-jvm:2.5.0-alpha10")
    ksp("androidx.room:room-compiler:2.7.0-alpha02")

    // SQLite
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.0-alpha05")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Common module
    implementation(project(":common"))

}

tasks.test {
    useJUnitPlatform()
}