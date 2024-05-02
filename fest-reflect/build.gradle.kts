
plugins {
    id("tokyo.northside.java-conventions")
}

dependencies {
    implementation(libs.assertj)
    testImplementation(libs.junit4)
    testImplementation(libs.mockito)
}

description = "FEST-reflect"

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "fest-reflect"
            description = "FEST-reflect"
        }
    }
}
