
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    api(libs.assertj)
    api(project(":fest-reflect"))
    implementation(libs.annotations)
    implementation(project(":fest-util"))
    testImplementation(project(":fest-test"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit4)
    testImplementation(libs.mockito)
    testImplementation(libs.equals.verifier)
}

description = "AssertJ-Swing"

val envIsCi: String? by project

tasks.named<Test>("test") {
    enabled = ("true" != envIsCi)
}

