dependencies {
    implementation(project(":model"))
    implementation(project(":usecase"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:6.0.5")
    
    // H2 Database for development
    runtimeOnly("com.h2database:h2")
}
