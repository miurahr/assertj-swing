import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.ConcurrentHashMap

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
    if (!isLinux()) {
        return false
    }
    return providers.exec {
        commandLine("sh", "-c", "command -v $command")
        isIgnoreExitValue = true
    }.result.get().exitValue == 0
}

abstract class XvfbService : BuildService<BuildServiceParameters.None> {
    private val pidByDisplay = ConcurrentHashMap<String, String>()

    fun setPid(display: String, pid: String?) {
        if (pid.isNullOrBlank()) {
            pidByDisplay.remove(display)
        } else {
            pidByDisplay[display] = pid
        }
    }

    fun getPid(display: String): String? {
        return pidByDisplay[display]
    }

    fun hasPid(display: String): Boolean {
        return pidByDisplay.containsKey(display)
    }
    fun allDisplays(): Set<String> = pidByDisplay.keys.toSet()
}

val xvfbService = gradle.sharedServices.registerIfAbsent(
    "xvfbService",
    XvfbService::class
) {}

val display = when (project.name) {
    "assertj-swing" -> "100"
    "assertj-swing-junit" -> "101"
    "assertj-swing-junit-jupiter" -> "102"
    else -> null
}

val testFinally by tasks.register("testFinally") {
    outputs.upToDateWhen { false }
    doLast {
        val service = xvfbService.get()
        display?.let {
            if (service.hasPid(it)) {
                val pid = service.getPid(display)
                logger.lifecycle("Stopping virtual X server at PID $pid ...")
                try {
                    val outputStream = ByteArrayOutputStream()
                    val errStream = ByteArrayOutputStream()
                    val injected = project.objects.newInstance<InjectedExecOps>()
                    val result = injected.execOps.exec {
                        commandLine("sh", "-c", "kill $pid")
                        standardOutput = outputStream
                        errorOutput = errStream
                        setIgnoreExitValue(true)
                    }
                    if (result.exitValue == 0) {
                        logger.lifecycle("Stopped virtual X server at PID $pid successfully.")
                    } else if (result.exitValue == 1) {
                        logger.warn("Virtual X server (PID: ${pid}) was not found (may have already stopped)")
                    } else {
                        logger.warn("Failed to stop virtual X server: $errStream")
                    }
                    Thread.sleep(500)
                    try {
                        injected.execOps.exec {
                            commandLine("sh", "-c", "kill -9 $pid")
                            standardOutput = ByteArrayOutputStream()
                            errorOutput = ByteArrayOutputStream()
                            setIgnoreExitValue(true)
                        }
                    } catch (e: Exception) {
                        logger.debug("Failed to kill virtual X server: ${e.message}")
                    }
                    service.setPid(display, null)
                } catch (e: Exception) {
                    logger.error("Error stopping virtual X server: ${e.message}")
                }
            }
        }
    }
}

tasks.test {
    onlyIf {
        isLinux() && isCommandAvailable("Xvfb") && isCommandAvailable("fluxbox")
    }

    val xvfbStarted = AtomicReference(false)
    doFirst {
        if (display != null) {
            val lockFile = File("/tmp/.X$display-lock")
            if (!lockFile.exists()) {
                try {
                    val outputStream = ByteArrayOutputStream()
                    val errStream = ByteArrayOutputStream()
                    val injected = project.objects.newInstance<InjectedExecOps>()
                    val result = injected.execOps.exec {
                        commandLine("sh", "-c", "Xvfb :$display -screen 0 1280x1024x24 >>/dev/null 2>&1 & echo $!")
                        standardOutput = outputStream
                        errorOutput = errStream
                        setIgnoreExitValue(true)
                    }
                    if (result.exitValue.equals(0)) {
                        val xvfbPid = outputStream.toString().trim()
                        if (xvfbPid.isNotEmpty() && xvfbPid.matches(Regex("[0-9]+"))) {
                            val checkResult = injected.execOps.exec {
                                commandLine("sh", "-c", "kill -0 $xvfbPid 2>/dev/null")
                                setIgnoreExitValue(true)
                            }
                            if (checkResult.exitValue == 0) {
                                xvfbService.get().setPid(display, xvfbPid)
                                xvfbStarted.set(true)
                                environment["DISPLAY"] = ":$display"
                                injected.execOps.exec {
                                    commandLine("sh", "-c", "fluxbox -display :$display -log /dev/null >>/dev/null &")
                                    standardOutput = outputStream
                                    errorOutput = outputStream
                                }
                                logger.lifecycle("Virtual X server is started with DISPLAY :$display and PID: $xvfbPid")
                            }
                        }
                    }
                } catch (e: Exception) {
                    logger.error("Error starting virtual X server: ${e.message}")
                }
            }
        }
    }
    finalizedBy(testFinally)
}

// Configure all Test tasks to optionally ignore failures by default.
// Developers can enforce failures by setting one of the following:
//   -PenforceTestFailures=true (Gradle project property)
//   -DenforceTestFailures=true (JVM system property)
//   ENFORCE_TEST_FAILURES=true (environment variable)
val enforceTestFailures: Boolean =
    (findProperty("enforceTestFailures") as String?)?.toBooleanStrictOrNull()
        ?: System.getProperty("enforceTestFailures")?.toBooleanStrictOrNull()
        ?: (System.getenv("ENFORCE_TEST_FAILURES")?.equals("true", ignoreCase = true) ?: false)

tasks.withType<Test>().configureEach {
    // By default, ignore failures only for the core module ':assertj-swing'.
    // Other modules will fail on test failures unless enforcement is explicitly disabled.
    val defaultIgnore = (project.name == "assertj-swing")
    // When enforcement is requested, do not ignore failures anywhere.
    ignoreFailures = if (enforceTestFailures) false else defaultIgnore
}
