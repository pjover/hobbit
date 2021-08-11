import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.20"
	kotlin("plugin.spring") version "1.5.20"
	id("com.google.cloud.tools.jib") version "1.3.0"
	id("info.solidsoft.pitest") version "1.5.2"
}

group = "cat.hobbiton"
version = version()

java.sourceCompatibility = JavaVersion.VERSION_16

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
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.3") // Java Simplified Encryption
	implementation("org.apache.poi:poi-ooxml:4.1.2") // Spread sheet generation

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(mapOf("group" to "org.junit.vintage", "module" to "junit-vintage-engine"))
	}
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.10")
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
	testImplementation("io.mockk:mockk:1.10.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "16"
	}
}

jib {
	from {
		image = "openjdk:8-jre-alpine"
	}
	to {
		tags = tags()
	}
	container {
		useCurrentTimestamp = true
		labels = mapOf(
			"org.opencontainers.image.title" to "Hobbit",
			"org.opencontainers.image.description" to "Hobbit Kotlin Spring boot application for managing a Kindergarten business",
			"org.opencontainers.image.version" to "$version",
			"org.opencontainers.image.url" to "https://github.com/pjover/hobbit",
			"org.opencontainers.image.vendor" to "https://github.com/pjover",
			"org.opencontainers.image.licenses" to "GPL-3.0"
		)
		workingDirectory = "/opt/target"
		jvmFlags = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005")
		ports = listOf("8080", "5005")
	}
}

fun version(): String {
	return rootProject
		.file("hobbit.yaml")
		.readLines()
		.findLast { it.startsWith("appVersion:") }
		?.split(":")
		?.last()
		?.trim()!!
}

fun tags(): Set<String> {
	return if(version.toString().endsWith("-SNAPSHOT")) {
		setOf("$version")
	} else {
		setOf("$version", "latest")
	}
}

pitest {
	outputFormats.add("HTML")
	timestampedReports.set(true)
	useClasspathFile.set(true)
	junit5PluginVersion.set("0.12")
	excludedClasses.add("cat.hobbiton.hobbit.HobitApplication*")
	excludedClasses.add("cat.hobbiton.hobbit.api.*")
}