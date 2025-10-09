plugins {
    kotlin("jvm") version "1.9.25"
}

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}
