plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    api(libs.commons.codec)
    api(libs.jupiter)
    api(project(":assertj-swing"))
    api(project(":assertj-swing-junit"))
    api(project(":fest-reflect"))
    compileOnly(libs.ant.junit)
    testImplementation(project(":fest-test"))
    testImplementation(libs.equals.verifier)
    testImplementation(libs.mockito)
    testImplementation(libs.ant.junit)
    testImplementation(libs.jupiter)
}

description = "AssertJ-Swing - JUnit Jupiter Extension"

tasks.named<Test>("test") {
    useJUnitPlatform()
}
