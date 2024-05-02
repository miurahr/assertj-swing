plugins {
    id("tokyo.northside.java-conventions")
    eclipse
    alias(libs.plugins.spotless)
}

dependencies {
    api(libs.jupiter)
    api(libs.jupiter.engine)
    api(libs.assertj)
    api(project(":assertj-swing"))
    api(project(":assertj-swing-junit"))
    testImplementation(libs.mockito)
    testImplementation(libs.jupiter)
}

description = "AssertJ-Swing - JUnit Jupiter Extension"

val envIsCi: String? by project
tasks.named<Test>("test") {
    useJUnitPlatform()
    if ("true" == envIsCi) {
        systemProperties.set("envIsCi", "true")
    }
    maxParallelForks =  1
    jvmArgs("-Xmx2048m", "--add-opens", "java.desktop/javax.swing=ALL-UNNAMED")
}

spotless {
    java {
        eclipse().configFile(file("${rootDir}/config/eclipse/assertj-eclipse-formatter.xml"))
        removeUnusedImports()
    }
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "assertj-swing-junit-jupiter"
            description = "Assertj-Swing JUnit5 Jupiter extension"
        }
    }
}
