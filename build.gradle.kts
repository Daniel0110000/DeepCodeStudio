import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "dev.daniel"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")

}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                val lwjglVersion = "3.3.1"
                listOf("lwjgl", "lwjgl-nfd").forEach { lwjglDep ->
                    implementation("org.lwjgl:${lwjglDep}:${lwjglVersion}")
                    listOf(
                        "natives-windows", "natives-windows-x86", "natives-windows-arm64",
                        "natives-macos", "natives-macos-arm64",
                        "natives-linux", "natives-linux-arm64", "natives-linux-arm32"
                    ).forEach { native ->
                        runtimeOnly("org.lwjgl:${lwjglDep}:${lwjglVersion}:${native}")
                    }
                }

                implementation("commons-io:commons-io:2.14.0")

                // Koin
                implementation("io.insert-koin:koin-core:3.5.0")

                // SQLite
                implementation("org.xerial:sqlite-jdbc:3.30.1")

                // Exposed
                implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")

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

                implementation("com.formdev:flatlaf:3.2.5")


            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DeepCodeStudio"
            packageVersion = "1.0.0"
        }
    }
}
