plugins {
    java
    `jacoco`
    id("org.sonarqube")

}

repositories {
    mavenCentral()
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.3")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

sonar {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir.parentFile.path}/build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml")
    }
}
