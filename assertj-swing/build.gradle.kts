
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    api(libs.assertj)
    api("com.google.code.findbugs:jsr305:3.0.2")
    api(project(":fest-reflect"))
    implementation(project(":fest-util"))
    testImplementation(project(":fest-test"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit4)
    testImplementation(libs.mockito)
    testImplementation(libs.equals.verifier)
    testImplementation(project(":multithreadedtc"))
}

description = "AssertJ-Swing"
