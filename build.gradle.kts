

plugins {
	application
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.sonarqube") version "5.1.0.4882"
	jacoco
}

group = "com.softdevsix"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val springdocVersion = "2.6.0"
val lombokVersion = "1.18.28"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")
	compileOnly ("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor ("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
	mainClass.set("com.softdevsix.argos.ArgosApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
	systemProperty("spring.profiles.active", "test")
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
		csv.required = false
		html.required = true
	}
}

sonar {
	val sonarProjectKey = System.getenv("SONAR_PROJECT_KEY") ?: ""
	val sonarHostUrl = System.getenv("SONAR_HOST_URL") ?: ""
	val sonarToken = System.getenv("SONAR_TOKEN") ?: ""
	properties {
		property("sonar.projectKey", sonarProjectKey)
		property("sonar.host.url", sonarHostUrl)
		property("sonar.token", sonarToken)
		property("sonar.qualitygate.wait", "true")
	}
}
