plugins {
    id("buildlogic.java-library-conventions")
    alias(libs.plugins.springboot.web) apply true
    alias(libs.plugins.dependency.management) apply true
    antlr
}

dependencies {
    implementation(libs.springboot.starter.web)
    implementation(libs.springdoc.openapi)
    developmentOnly(libs.springboot.devtools)
    testImplementation(libs.springboot.starter.test)
    testRuntimeOnly(libs.junit.launcher)
    antlr(libs.antlr)
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
