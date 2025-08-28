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

interface InjectedExecOps {
    @get:Inject val execOps: ExecOperations
}

// Function to detect if the OS is Linux
fun isLinux(): Boolean = System.getProperty("os.name").lowercase().contains("linux")

// Function to check if Xvfb is installed
fun isCommandAvailable(command: String): Boolean {
    return providers.exec {
        commandLine("sh", "-c", "command -v $command")
    }.result.get().exitValue.equals(0)
}

project.extensions.extraProperties["xvfbPid"] = ""

val testFinally by tasks.register("testFinally") {
    outputs.upToDateWhen { false }
    doLast {
        val pid = project.extensions.extraProperties["xvfbPid"] as String
        if (pid.isNotBlank()) {
            val injected = project.objects.newInstance<InjectedExecOps>()
            println("Stopping virtual X server at PID $pid ...")
            val outputStream = ByteArrayOutputStream()
            injected.execOps.exec {
                commandLine("sh", "-c", "kill $pid &")
                standardOutput = outputStream
                errorOutput = outputStream
            }
            project.extensions.extraProperties["xvfbPid"] = ""
        }
    }
}

tasks.test {
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
            val lockFile = File("/tmp/.X$display-lock")
            if (!lockFile.exists()) {
                val outputStream = ByteArrayOutputStream()
                val injected = project.objects.newInstance<InjectedExecOps>()
                val res = injected.execOps.exec {
                    commandLine("sh", "-c", "Xvfb :$display -screen 0 1280x1024x24 >>/dev/null 2>&1 & echo $!")
                    standardOutput = outputStream
                }.exitValue
                if (res.equals(0)) {
                    val xvfbPid = outputStream.toString().trim()
                    project.extensions.extraProperties["xvfbPid"] = xvfbPid
                    environment["DISPLAY"] = ":$display"
                    injected.execOps.exec {
                        commandLine("sh", "-c", "fluxbox -display :$display -log /dev/null >>/dev/null &")
                        standardOutput = outputStream
                        errorOutput = outputStream
                    }
                    println("Virtual X server is started with DISPLAY :$display and PID: $xvfbPid")         }
                }
        }
    }
    finalizedBy(testFinally)
}
