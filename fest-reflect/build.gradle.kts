
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    implementation(project(":fest-util"))
    testImplementation(libs.junit4)
    testImplementation(project(":fest-assert"))
    testImplementation(project(":fest-test"))
    testImplementation(libs.mockito)
}

description = "FEST-reflect"
