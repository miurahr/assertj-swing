pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("com.gradle.develocity") version("3.17.2")
}
develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
    }
}

rootProject.name = "assertj-swing-parent"
include(":assertj-swing")
include(":assertj-swing-junit")
include(":assertj-swing-junit-jupiter")
include(":fest-reflect")