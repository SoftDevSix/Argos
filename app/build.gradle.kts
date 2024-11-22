plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.springboot.web) apply true
    alias(libs.plugins.dependency.management) apply true
}

dependencies {
    implementation(libs.springboot.starter.web)
    implementation(libs.springdoc.openapi)
    developmentOnly(libs.springboot.devtools)
    testImplementation(libs.springboot.starter.test)
    testRuntimeOnly(libs.junit.launcher)
}

application {
    mainClass.set("edu.usb.argos.ArgosApplication")
}
