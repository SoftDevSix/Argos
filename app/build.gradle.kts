plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.springboot.framework) apply true
    alias(libs.plugins.dependency.management) apply true
    alias(libs.plugins.lombok) apply true
}

dependencies {
    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.jpa)
    implementation(libs.springdoc.openapi.webmvc.ui)
    implementation(libs.postgresql)
    developmentOnly(libs.springboot.devtools)
    testImplementation(libs.springboot.starter.test)
    testImplementation(libs.junit.launcher)
    testRuntimeOnly(libs.h2database)
}

application {
    mainClass.set("edu.usb.argos.ArgosApplication")
}

tasks.withType<Test> {
        systemProperty("spring.profiles.active", "test")
}
