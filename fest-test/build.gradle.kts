
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    implementation(libs.junit4)
    implementation(project(":fest-util"))
    implementation("org.jetbrains:annotations:23.0.0")
}

description = "FEST-test"
