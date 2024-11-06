plugins {
    id("java")
}

group = "dev.daniel"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // RSyntaxTextArea
    implementation("com.fifesoft:rsyntaxtextarea:3.5.1")

    // Common module
    implementation(project(":common"))
}

tasks.test {
    useJUnitPlatform()
}