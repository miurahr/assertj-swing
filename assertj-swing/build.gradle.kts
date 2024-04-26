
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    api(libs.assertj)
    implementation(project(":fest-reflect"))
    implementation(libs.annotations)
    testImplementation(libs.assertj)
    testImplementation(libs.junit4)
    testImplementation(libs.mockito)
    testImplementation(libs.equals.verifier)
}

description = "AssertJ-Swing"

val envIsCi: String? by project

tasks.named<Test>("test") {
    if ("true" == envIsCi) {
        filter {
            includeTestsMatching("org.assertj.core.api.*")
            includeTestsMatching("org.assertj.swing.core.*")
        }
    }
    maxParallelForks =  1
}

