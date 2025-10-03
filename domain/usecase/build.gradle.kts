dependencies {
    implementation(project(":model"))
    // Solo depende del Model, sin otras dependencias externas según las reglas de Clean Architecture
    
    // Testing dependencies
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}
