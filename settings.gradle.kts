rootProject.name = "logistic-information-manager"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version "1.9.25"
        kotlin("plugin.spring") version "1.9.25"
        kotlin("plugin.allopen") version "1.9.25"
        kotlin("kapt") version "1.9.25"
        id("io.spring.dependency-management") version "1.1.7"
        id("org.springframework.boot") version "3.5.6"
        id("org.sonarqube") version "6.3.1.5724"
        id("jacoco")
    }
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
    }
}

include(":app-service")
project(":app-service").projectDir = file("./applications/app-service")

include(":model")
project(":model").projectDir = file("./domain/model")

include(":usecase")
project(":usecase").projectDir = file("./domain/usecase")