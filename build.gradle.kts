import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight") version "2.0.0"
}

group = "dev.daniel"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")

}

sqldelight{
    databases{
        create("AppDatabase"){
            packageName.set("dev.daniel.database")
        }
    }
}

dependencies {

    implementation(compose.desktop.currentOs)

    // Commons
    implementation("commons-io:commons-io:2.14.0")

    // Koin
    implementation("io.insert-koin:koin-core:3.5.0")

    // SQLite
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")

    // Gson & Json
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20231013")

    // MVVM
    implementation("dev.icerock.moko:mvvm-compose:0.16.1")
    implementation("dev.icerock.moko:mvvm-flow-compose:0.16.1")
    implementation("dev.icerock.moko:mvvm-livedata-compose:0.16.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    // Jediterm
    implementation("org.jetbrains.jediterm:jediterm-pty:2.42")

    // Pty4j
    implementation("org.jetbrains.pty4j:pty4j:0.12.13")

    // Log4j
    implementation("log4j:log4j:1.2.17")

    // Guava
    implementation("com.google.guava:guava:32.1.3-jre")

    // Flatlaf
    implementation("com.formdev:flatlaf:3.2.5")
    implementation("com.formdev:flatlaf-intellij-themes:3.2.5")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DeepCodeStudio"
            packageVersion = "1.0.0"
        }
    }
}
