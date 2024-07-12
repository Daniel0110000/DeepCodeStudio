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

    // Gson & Json
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20231013")

    // Commons
    implementation("commons-io:commons-io:2.14.0")

    // Flatlaf
    implementation("com.formdev:flatlaf:3.2.5")
    implementation("com.formdev:flatlaf-intellij-themes:3.2.5")

    // Mock
    testImplementation("io.mockk:mockk:1.13.10")

    // JUnit
    testImplementation("junit:junit:4.12")

    // Truth
    testImplementation("com.google.truth:truth:1.4.3")

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1-Beta")

}