// Define the plugins used in the project: Java and Application for basic app setup.
plugins {
    java
    application
}

// Set the group ID and version of this Gradle project.
group = "com.example"
version = "1.0-SNAPSHOT"

// Specify the Java version to ensure compatibility with the development environment.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// Declare the repository to be used for fetching the dependencies.
repositories {
    mavenCentral() // Maven Central is widely used and contains a vast number of third-party libraries.
}

// Define the dependencies required by both the production and test environments.
dependencies {
    // Core dependencies for running the Spark-based web service.
    implementation("com.sparkjava:spark-core:2.9.3")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.json:json:20210307")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Dependencies specifically for testing purposes to mock and assert behaviours.
    testImplementation("com.github.tomakehurst:wiremock:2.27.2")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.27.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:3.5.15")
    testImplementation("org.mockito:mockito-junit-jupiter:3.5.15")
    testImplementation("org.mockito:mockito-inline:3.5.15") // Allows mocking of static methods.
}

// Configure the test tasks to use JUnit Platform, which supports running JUnit 5 tests.
tasks.test {
    useJUnitPlatform()
}

// Define the main class to be executed when the application starts.
application {
    mainClass.set("partA_Web_Service_API.App")
}
