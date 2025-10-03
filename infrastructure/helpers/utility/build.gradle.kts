dependencies {
    implementation(project(":model"))
    implementation(project(":usecase"))
    
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-crypto")
}
