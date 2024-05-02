
plugins {
    id("tokyo.northside.java-conventions")
    eclipse
    alias(libs.plugins.spotless)
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
            // exclude time consuming tests
            excludeTestsMatching("org.assertj.swing.applet.*")
            excludeTestsMatching("org.assertj.swing.finder.*")
            excludeTestsMatching("org.assertj.swing.fixture.*")
            excludeTestsMatching("org.assertj.swing.format.*")
            excludeTestsMatching("org.assertj.swing.keystroke.*")
            excludeTestsMatching("org.assertj.swing.lock.*")
            excludeTestsMatching("org.assertj.swing.monitor.*")
            excludeTestsMatching("org.assertj.swing.security.*")
            excludeTestsMatching("org.assertj.swing.test.*")
            excludeTestsMatching("org.assertj.swing.timing.*")
            excludeTestsMatching("org.assertj.swing.util.*")
        }
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
            name = "assertj-swing"
            description = "Assertj-Swing test framework"
        }
    }
}
