plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "dev.daniel"
version = "1.0.0"

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {

    implementation(compose.desktop.currentOs)

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

    // Koin
    implementation("io.insert-koin:koin-core:3.5.0")

    // Common module
    implementation(project(":common"))

}

tasks.test {
    useJUnitPlatform()
}