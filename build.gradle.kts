plugins {
	application
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.sonarqube") version "5.1.0.4882"
}

group = "com.softdevsix"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
	mainClass.set("com.softdevsix.argos.ArgosApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
