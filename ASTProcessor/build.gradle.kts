plugins {
	application
	alias(libs.plugins.springboot.web) apply true
	alias(libs.plugins.dependency.management) apply true
	antlr
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
	implementation(libs.antlr.runtime)
	developmentOnly(libs.springboot.devtools)
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	antlr(libs.antlr)
	testImplementation(libs.logback)
	testCompileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)
	testImplementation(libs.springboot.starter.test)
	testRuntimeOnly(libs.junit.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val generateLexerSource by tasks.registering(AntlrTask::class) {
	maxHeapSize = "64m"
	source = fileTree("src/main/antlr") { include("JavaLexer.g4") }
	arguments = listOf("-visitor", "-package", "edu.usb.argos.ASTProcessor.antlr", "-encoding", "UTF-8")
	outputDirectory = file("src/main/java/edu/usb/argos/ASTProcessor/antlr")
}

val generateParserSource by tasks.registering(AntlrTask::class) {
	maxHeapSize = "64m"
	source = fileTree("src/main/antlr") { include("JavaParser.g4") }
	arguments = listOf("-visitor", "-package", "edu.usb.argos.ASTProcessor.antlr", "-encoding", "UTF-8")
	outputDirectory = file("src/main/java/edu/usb/argos/ASTProcessor/antlr")
	dependsOn(generateLexerSource)
}

tasks.compileJava {
	dependsOn(generateLexerSource, generateParserSource)
}
