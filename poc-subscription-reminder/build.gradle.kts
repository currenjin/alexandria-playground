plugins {
    kotlin("jvm") version "1.9.25"
}

group = "com.currenjin"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runDemo") {
    group = "application"
    description = "Run the subscription reminder demo."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.currenjin.DemoKt")

    val todayArg = project.findProperty("today") as String?
    if (!todayArg.isNullOrBlank()) {
        args(todayArg)
    }
}
