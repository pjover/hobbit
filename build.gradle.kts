import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
}

group = "cat.hobbiton"

java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springdoc:springdoc-openapi-ui:1.4.8")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.4.8")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.4.8")

    implementation("com.itextpdf:itextpdf:5.5.13.2") // PDF generation with iText
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4") // Java Simplified Encryption
    implementation("org.apache.poi:poi-ooxml:5.0.0") // Spread sheet generation


    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6") {
        exclude(mapOf("group" to "org.junit.vintage", "module" to "junit-vintage-engine"))
    }
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.2.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.10")
//    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.3")
    testImplementation("io.kotest:kotest-runner-console-jvm:4.1.3.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.3")
    testImplementation("io.kotest:kotest-extensions-spring-jvm:4.4.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
