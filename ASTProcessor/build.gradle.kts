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
	developmentOnly(libs.springboot.devtools)
	testImplementation(libs.springboot.starter.test)
	testRuntimeOnly(libs.junit.launcher)
	antlr(libs.antlr)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    source = fileTree("src/main/antlr") {
        include("**/*.g4")
    }
    outputDirectory = file("src/main/java/edu/usb/argos/ASTProcessor/antlr")
    arguments = listOf(
        "-visitor",
        "-package", "edu.usb.argos.ASTProcessor.antlr",
        "-encoding", "UTF-8"
    )
}

tasks.compileJava {
	dependsOn(tasks.generateGrammarSource)
}
