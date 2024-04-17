plugins {
    kotlin("jvm")
    id("app.cash.sqldelight") version "2.0.0"
}

group = "dev.daniel"
version = "1.0.0"

repositories {
    mavenCentral()
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("app.deepCodeStudio.database")
        }
    }
}

dependencies {

    // Koin
    implementation("io.insert-koin:koin-core:3.5.0")

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