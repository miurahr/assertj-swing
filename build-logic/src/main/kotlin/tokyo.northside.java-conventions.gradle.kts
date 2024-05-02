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
version = "4.0.0-SNAPSHOT"

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
                url.set("https://github.com/omegat-org/assertj-swing")
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
                    connection.set("scm:git:git://github.com/omegat-org/assertj-swing.git")
                    developerConnection.set("scm:git:git://github.com/omegat-org/assertj-swing.git")
                    url.set("https://github.com/omegat-org/assertj-swing")
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
