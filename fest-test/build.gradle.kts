
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    implementation(libs.junit4)
    implementation(project(":fest-util"))
    implementation(libs.annotations)
}

description = "FEST-test"
