plugins {
    `maven-publish`
    signing
    alias(libs.plugins.nexus.publish)
}

nexusPublishing.repositories {
    sonatype()
}
