plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "dev.daniel"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(compose.desktop.currentOs)

    // MVVM
    implementation("dev.icerock.moko:mvvm-compose:0.16.1")
    implementation("dev.icerock.moko:mvvm-flow-compose:0.16.1")
    implementation("dev.icerock.moko:mvvm-livedata-compose:0.16.1")

    // Koin
    implementation("io.insert-koin:koin-core:3.5.0")

    // Common module
    implementation(project(":common"))

    // Database module
    implementation(project(":database"))

}

tasks.test {
    useJUnitPlatform()
}