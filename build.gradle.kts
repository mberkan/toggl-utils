plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    application
}

group = "pl.mberkan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // Test libraries
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("org.mockito:mockito-core:3.8.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")


    // https://mvnrepository.com/artifact/io.github.openfeign/feign-core (REST API)
    implementation("io.github.openfeign:feign-core:11.1")
    implementation("io.github.openfeign:feign-gson:11.1")
    implementation("io.github.openfeign:feign-slf4j:11.1")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api (logging)
    implementation("org.slf4j:slf4j-api:1.7.30")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
    runtimeOnly("ch.qos.logback:logback-core:1.2.3")

    // https://mvnrepository.com/artifact/com.sun.mail/javax.mail
    implementation("com.sun.mail:javax.mail:1.6.2")

}

application {
    // Define the main class for the application.
    mainClass.set("pl.mberkan.toggl.AppKt")
}

tasks.test {
    useJUnitPlatform()
}
