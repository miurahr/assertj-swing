plugins {
    id("tokyo.northside.java-conventions")
    eclipse
    alias(libs.plugins.spotless)
}

dependencies {
    api(libs.jupiter)
    api(libs.assertj)
    api(project(":assertj-swing"))
    api(project(":assertj-swing-junit"))
    testImplementation(libs.mockito)
    testImplementation(libs.jupiter)
}

description = "AssertJ-Swing - JUnit Jupiter Extension"

tasks.named<Test>("test") {
    useJUnitPlatform()
}

spotless {
    java {
        eclipse().configFile(file("${rootDir}/config/eclipse/assertj-eclipse-formatter.xml"))
        removeUnusedImports()
    }
}
