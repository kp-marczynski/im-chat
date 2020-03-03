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
    if (!project.gradle.startParameter.taskNames.contains("test")) {
        dependsOn("angularBuild")
    }
}

fun setupAngularEnv(context: Exec) {
    context.workingDir(webappDir)
    context.inputs.dir(webappDir)
    // Add task to the standard build group
    context.group = "angular"
}

fun runNpmCommand(context: Exec, command: String) {
    setupAngularEnv(context)
    // ng doesn't exist as a file in windows -> ng.cmd
    val npmAlias = if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
        "npm.cmd"
    } else {
        "npm"
    }
    context.commandLine(listOf(npmAlias, command))
}

tasks.register<Exec>("angularBuild") {
    runNpmCommand(this, "build")
}

tasks.register<Exec>("angularInstall") {
    runNpmCommand(this, "install")
}

tasks.register<Exec>("angularStart") {
    runNpmCommand(this, "start")
}

tasks.register<Exec>("angularTestUnit") {
    runNpmCommand(this, "test")
}

tasks.register<Exec>("angularTestE2E") {
    runNpmCommand(this, "e2e")
}