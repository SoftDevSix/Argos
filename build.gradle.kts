plugins {
    `java`
    `jacoco`
    id("org.sonarqube") version "5.1.0.4882"
}

repositories {
    mavenCentral()
}

// tag::coverageTask[]
tasks.register<JacocoReport>("codeCoverageReport") {
    subprojects {
        val subproject = this
        subproject.plugins.withType<JacocoPlugin>().configureEach {
            subproject.tasks.matching({ it.extensions.findByType<JacocoTaskExtension>() != null }).configureEach {
                val testTask = this
                sourceSets(subproject.sourceSets["main"])
                executionData(testTask)
            }
        }
    }

    reports {
        xml.required = true
        html.required = false
    }
}
// end::coverageTask[]

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
