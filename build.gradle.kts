plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "dev.daniel"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")

}

dependencies {

    implementation(compose.desktop.currentOs)

    // Commons
    implementation("commons-io:commons-io:2.14.0")

    // Koin
    implementation("io.insert-koin:koin-core:3.5.0")

    // MVVM
    implementation("dev.icerock.moko:mvvm-compose:0.16.1")
    implementation("dev.icerock.moko:mvvm-flow-compose:0.16.1")
    implementation("dev.icerock.moko:mvvm-livedata-compose:0.16.1")

    // Database module
    implementation(project(":database"))

    // Settings module
    implementation(project(":settings"))

    // Common module
    implementation(project(":common"))

    // Terminal module
    implementation(project(":terminal"))

    // Editor module
    implementation(project(":editor"))

}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            modules("java.sql")
            packageName = "DeepCodeStudio"
            packageVersion = "1.0.0"
            vendor = "Dr10 Technology"
            copyright = "© 2023 Dr10 Technology. All rights reserved."
            licenseFile.set(project.file("LICENSE"))
            windows{ iconFile.set(project.file("ic_launcher.ico")) }
            linux{ iconFile.set(project.file("ic_launcher.png")) }
        }
    }
}
