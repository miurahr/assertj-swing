
plugins {
    id("tokyo.northside.java-conventions")
    eclipse
    alias(libs.plugins.spotless)
}

dependencies {
    api(libs.junit4)
    api(project(":assertj-swing"))
    implementation(libs.commons.codec)
    implementation(libs.commons.io)
    implementation(project(":fest-reflect"))
    compileOnly(libs.ant.junit)
    testImplementation(project(":assertj-swing"))
    testImplementation(libs.assertj)
    testImplementation(libs.mockito)
    testImplementation(libs.equals.verifier)
    testImplementation(libs.ant.junit)
}

description = "AssertJ-Swing - JUnit Extension"

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "org.assertj.swing.junit")
    }
}

spotless {
    java {
        eclipse().configFile(file("${rootDir}/config/eclipse/assertj-eclipse-formatter.xml"))
        removeUnusedImports()
    }
}

val envIsCi: String? by project
tasks.named<Test>("test") {
    if ("true" == envIsCi) {
        systemProperties.set("envIsCi", "true")
    }
    maxParallelForks =  1
    jvmArgs("-Xmx2048m", "--add-opens", "java.desktop/javax.swing=ALL-UNNAMED")
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "assertj-swing-junit"
            description = "Assertj-Swing JUnit4 extension"
        }
    }
}
