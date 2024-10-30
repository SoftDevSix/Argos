plugins {
	application
	alias(libs.plugins.springboot.web) apply true
	alias(libs.plugins.dependency.management) apply true
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
	implementation(libs.springboot.starter.web)
	implementation(libs.springdoc.openapi)
	developmentOnly(libs.springboot.devtools)
	testImplementation(libs.springboot.starter.test)
	testRuntimeOnly(libs.junit.launcher)
}

application {
	mainClass.set("com.softdevsix.argos.ArgosApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
