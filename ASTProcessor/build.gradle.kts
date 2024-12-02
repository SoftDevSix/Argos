plugins {
	application
	alias(libs.plugins.springboot.web) apply true
	alias(libs.plugins.dependency.management) apply true
	alias(libs.plugins.sonarqube) apply true
	antlr
	jacoco
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
	implementation(libs.springdoc.openapi)
	implementation(libs.slf4j.api)
	implementation(libs.antlr.runtime)
	developmentOnly(libs.springboot.devtools)
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	antlr(libs.antlr)
	testImplementation(libs.logback)
	testImplementation(libs.springboot.starter.test)
	testCompileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)
	testRuntimeOnly(libs.junit.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val generateLexerSource by tasks.registering(AntlrTask::class) {
	maxHeapSize = "64m"
	source = fileTree("src/main/antlr") { include("JavaLexer.g4") }
	arguments = listOf("-visitor", "-package", "edu.usb.argos.astprocessor.antlr", "-encoding", "UTF-8")
	outputDirectory = file("src/main/java/edu/usb/argos/astprocessor/antlr")
}

val generateParserSource by tasks.registering(AntlrTask::class) {
	maxHeapSize = "64m"
	source = fileTree("src/main/antlr") { include("JavaParser.g4") }
	arguments = listOf("-visitor", "-package", "edu.usb.argos.astprocessor.antlr", "-encoding", "UTF-8")
	outputDirectory = file("src/main/java/edu/usb/argos/astprocessor/antlr")
	dependsOn(generateLexerSource)
}

tasks.compileJava {
	dependsOn(generateLexerSource, generateParserSource)
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
