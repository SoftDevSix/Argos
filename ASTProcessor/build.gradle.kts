plugins {
	application
	alias(libs.plugins.springboot.web) apply true
	alias(libs.plugins.dependency.management) apply true
}

group = "edu.usb.argos"
version = "0.0.1-SNAPSHOT"

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.springboot.starter.web)
	implementation(libs.java.parser.core)
	implementation(libs.springdoc.openapi)
	developmentOnly(libs.springboot.devtools)
	testImplementation(libs.springboot.starter.test)
	testRuntimeOnly(libs.junit.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
