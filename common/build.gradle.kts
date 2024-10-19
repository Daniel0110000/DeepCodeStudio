plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "dev.daniel"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(compose.desktop.currentOs)

    // Gson & Json
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20231013")

    // Commons
    implementation("commons-io:commons-io:2.14.0")

    // Flatlaf
    implementation("com.formdev:flatlaf:3.2.5")
    implementation("com.formdev:flatlaf-intellij-themes:3.2.5")

    // Batik
    implementation("org.apache.xmlgraphics:batik-transcoder:1.17")
    implementation("org.apache.xmlgraphics:batik-codec:1.17")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    // JFlex
    implementation("de.jflex:jflex:1.4.1")

    // Java Parser
    implementation("com.github.javaparser:javaparser-core:3.26.2")

}

tasks.test {
    useJUnitPlatform()
}