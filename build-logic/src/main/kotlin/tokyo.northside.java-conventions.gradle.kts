import java.io.ByteArrayOutputStream

plugins {
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "tokyo.northside"
version = "4.0.0-beta-2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
    setFailOnError(false)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            setGroupId("tokyo.northside")
            pom {
                url.set("https://codeberg.org/miurahr/assertj-swing")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("miurahr")
                        name.set("Hiroshi Miura")
                        email.set("miurahr@linux.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://codeberg.org/miurahr/assertj-swing.git")
                    developerConnection.set("scm:git:git://codeberg.org/miurahr/assertj-swing.git")
                    url.set("https://codeberg.org/miurahr/assertj-swing")
                }
            }
        }
    }
}
val signKey = listOf("signingKey", "signing.keyId", "signing.gnupg.keyName").find {project.hasProperty(it)}
tasks.withType<Sign> {
    onlyIf { !version.toString().endsWith("-SNAPSHOT") && (signKey != null) }
}

signing {
    when (signKey) {
        "signingKey" -> {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
        "signing.keyId" -> {/* do nothing */}
        "signing.gnupg.keyName" -> {
            useGpgCmd()
        }
    }
    sign(publishing.publications["mavenJava"])
}

fun startX(display: String): String {
    val outputStream = ByteArrayOutputStream()
    exec {
        commandLine("sh", "-c", "Xvfb :" + display + " -screen 0 1280x1024x24 >>/dev/null 2>&1 & echo $!")
        standardOutput = outputStream
    }
    val xvfbPid = outputStream.toString().trim()
    exec {
        commandLine("sh", "-c", "fluxbox -display :" + display + " -log /dev/null >>/dev/null &")
        errorOutput = outputStream
    }
    println("Virtual X server is started with DISPLAY :$display and PID: $xvfbPid")
    return xvfbPid
}

fun stopX(pid: String) {
    println("Stopping virtual X server...")
    val outputStream = ByteArrayOutputStream()
    exec {
        commandLine("sh", "-c", "kill $pid")
        standardOutput = outputStream
        errorOutput = outputStream
    }
}

// Function to detect if the OS is Linux
fun isLinux(): Boolean = System.getProperty("os.name").lowercase().contains("linux")

// Function to check if Xvfb is installed
fun isCommandAvailable(command: String): Boolean {
    val outputStream = ByteArrayOutputStream()
    return exec {
        commandLine("sh", "-c", "command -v " + command)
        isIgnoreExitValue = true // Don't fail if command is not found
        standardOutput = outputStream
        errorOutput = outputStream
    }.exitValue.equals(0)
}

val test by tasks.existing(Test::class) {
    onlyIf {
        isLinux() && isCommandAvailable("Xvfb") && isCommandAvailable("fluxbox")
    }
    val display = when (project.name) {
        "assertj-swing" -> "100"
        "assertj-swing-junit" -> "101"
        "assertj-swing-junit-jupiter" -> "102"
        else -> null
    }
    doFirst {
        if (display != null) {
            val lockFile = File("/tmp/.X" + display + "-lock")
            if (!lockFile.exists()) {
                extensions.extraProperties["xvfbPid"] = startX(display)
                environment["DISPLAY"] = ":" + display
            }
        }
    }
    doLast {
        if (display != null) {
            val pid = extensions.extraProperties["xvfbPid"] as String?
            if (pid != null) {
                stopX(pid)
            }
        }
    }
}
