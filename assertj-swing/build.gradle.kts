
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

tasks.named<Test>("test") {
    useJUnit()
    val envIsCi = project.findProperty("envIsCi")?.toString()?.toBoolean() ?: false
    val includeSlowTests = project.findProperty("includeSlowTests")?.toString()?.toBoolean() ?: false
    if (envIsCi) {
        systemProperties.set("envIsCi", "true")
    }
    if (!includeSlowTests) {
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
            // individual slow tests
            excludeTestsMatching("org.assertj.swing.driver.Bug219_editTableCellWithEditorHavingCustomDocument_Test")
            excludeTestsMatching("org.assertj.swing.driver.ComponentDriver_pressAndReleaseKeys_Test")
            excludeTestsMatching("org.assertj.swing.driver.ComponentDriver_pressAndReleaseKeyWithPressInfo_Test")
            excludeTestsMatching("org.assertj.swing.driver.BasicJTableCellWriter_enterValue_Test")
            excludeTestsMatching("org.assertj.swing.driver.ComponentDriver_invokePopupAtPoint_Test")
            excludeTestsMatching("org.assertj.swing.driver.BasicJTableCellWriter_enterValue_Test")
            excludeTestsMatching("org.assertj.swing.driver.Bug225_pressF2ToStartEditingTableCell_Test")
            excludeTestsMatching("org.assertj.swing.core.WindowAncestorFinder_windowAncestorOf_Test")
            excludeTestsMatching("org.assertj.swing.core.FEST103_modifierNotBeingPressed_Test")
            excludeTestsMatching("org.assertj.swing.driver.JComboBoxDropDownListFinder_findDropDownList_Test")
        }
    }
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
