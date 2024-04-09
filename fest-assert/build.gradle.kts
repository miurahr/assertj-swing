
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    implementation(project(":fest-util"))
    testImplementation(project(":fest-test"))
    testImplementation(libs.junit4)
    testImplementation(libs.mockito)
}

description = "FEST-assert"
