import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.google.cloud.tools.jib") version "2.1.0"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

group = "com.imchat"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

// angular integration based on: https://blog.marcnuri.com/angular-spring-boot-integration-gradle/
val webappDir = "$projectDir/src/main/webapp"
sourceSets.getByName("main") {
    resources.srcDir("$webappDir/dist")
    resources.srcDir("$projectDir/src/main/resources")
}

tasks.processResources {
    dependsOn("buildAngular")
}

tasks.register<Exec>("buildAngular") {
    // installAngular should be run prior to this task
    dependsOn("installAngular")
    workingDir(webappDir)
    inputs.dir(webappDir)
    // Add task to the standard build group
    group = BasePlugin.BUILD_GROUP
    // ng doesn't exist as a file in windows -> ng.cmd
    if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
        commandLine("ng.cmd", "build")
    } else {
        commandLine(listOf("ng", "build"))
    }
}

tasks.register<Exec>("installAngular") {
    workingDir(webappDir)
    inputs.dir(webappDir)
    group = BasePlugin.BUILD_GROUP
    if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
        commandLine(listOf("npm.cmd", "install"))
    } else {
        commandLine(listOf("npm", "install"))
    }
}