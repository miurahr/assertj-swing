plugins {
    id("tokyo.northside.java-conventions")
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
