dependencies {
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.tngtech.archunit:archunit:1.4.1")
    implementation(project(":model"))
    implementation(project(":usecase"))

    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}