import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "battleship"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.6.21")

    implementation("org.springframework.security:spring-security-crypto:5.7.3")

    // JDBI
    implementation("org.jdbi:jdbi3-core:3.34.0")
    implementation("org.jdbi:jdbi3-json:3.34.0")
    implementation("org.jdbi:jdbi3-gson2:3.34.0")
    implementation("org.jdbi:jdbi3-sqlobject:3.34.0")
    implementation("org.jdbi:jdbi3-postgres:3.34.0")
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("org.jdbi:jdbi3-kotlin:3.34.0")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.34.0")
    runtimeOnly("org.postgresql:postgresql")
    //implementation("org.jetbrains.exposed:exposed-jdbc:0.24.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
