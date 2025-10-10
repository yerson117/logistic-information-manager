dependencies {
    implementation(project(":model"))
    implementation(project(":usecase"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:6.0.5")

    runtimeOnly("com.h2database:h2")
}
