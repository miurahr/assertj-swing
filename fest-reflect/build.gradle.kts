
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    implementation(libs.assertj)
    testImplementation(libs.junit4)
    testImplementation(project(":fest-test"))
    testImplementation(libs.mockito)
}

description = "FEST-reflect"
