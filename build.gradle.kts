plugins {
    kotlin("jvm")
    kotlin("plugin.spring") apply false
    kotlin("kapt")
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    id("org.sonarqube") apply true
    id("jacoco") apply true
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.sonarqube")
    apply(plugin = "jacoco")
    apply(plugin = "io.spring.dependency-management")

    java.sourceCompatibility = JavaVersion.VERSION_21

    // Dependencias comunes para todos los módulos
    dependencies {
        // Lombok
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
        testCompileOnly("org.projectlombok:lombok:1.18.38")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

        // JUnit 5 - Usar las versiones de Spring Boot BOM
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        
        testImplementation(kotlin("test"))
        testImplementation("io.mockk:mockk:1.13.3")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.6")
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
            allWarningsAsErrors = true
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    tasks.test {
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco.xml"))
            csv.required.set(false)
            html.outputLocation.set(layout.buildDirectory.dir("reports/jacocoHtml"))
        }
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}

configure(project.subprojects.filter { it == project(":app-service") }) {
    apply(plugin = "org.springframework.boot")

    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
    }
}

// Configuración para módulos de infraestructura y aplicación
configure(project.subprojects.filter { it != project(":model") && it != project(":usecase") }) {
    apply(plugin = "kotlin-spring")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        api("org.mapstruct:mapstruct:1.5.2.Final")
        kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")

        // JUnit 5 - Usar las versiones de Spring Boot BOM
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        
        testImplementation(kotlin("test"))
        testImplementation("io.mockk:mockk:1.13.3")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
        testImplementation("org.springframework.security:spring-security-test")
    }

    configurations {
        all {
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
        }
    }
}

jacoco {
    toolVersion = "0.8.13"
    reportsDirectory.set(layout.buildDirectory.dir("reports"))
}

tasks.register<JacocoReport>("jacocoMergedReport") {
    dependsOn(subprojects.map { project -> project.tasks.jacocoTestReport })
    additionalSourceDirs.setFrom(files(subprojects.map { project -> project.sourceSets.main.get().allSource.srcDirs }))
    sourceDirectories.setFrom(files(subprojects.map { project -> project.sourceSets.main.get().allSource.srcDirs }))
    classDirectories.setFrom(files(subprojects.map { project -> project.sourceSets.main.get().output }))
    executionData.setFrom(project.fileTree(layout.buildDirectory) { include("**/build/jacoco/test.exec") })
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

sonarqube {
    val modules = subprojects.map { subproject ->
        subproject.projectDir.toString().replace(project.projectDir.toString() + "/", "")
    }
    properties {
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.modules", modules.joinToString(","))
        property("sonar.sources", "src,settings.gradle.kts,build.gradle.kts,${modules.joinToString(",") { module -> "$module/build.gradle.kts" }}")
        property("sonar.test", "src/test")
        property("sonar.exclusions", "**/MainApplication.kt")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
    }
}

tasks.wrapper {
    gradleVersion = "8.14.3"
}