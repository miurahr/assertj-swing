
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    api(libs.commons.codec)
    api(libs.junit4)
    api(project(":assertj-swing"))
    api(project(":fest-reflect"))
    compileOnly(libs.ant.junit)
    testImplementation(project(":fest-test"))
    testImplementation(project(":assertj-swing"))
    testImplementation(libs.mockito)
    testImplementation(libs.equals.verifier)
    testImplementation(libs.ant.junit)
}

description = "AssertJ-Swing - JUnit Extension"
