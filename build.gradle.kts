// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath(GradlePlugin.Android.get())
        classpath(GradlePlugin.KotlinPlugin.get())
        classpath(GradlePlugin.GoogleServices.get())
        classpath(GradlePlugin.NavigationSafeArgs.get())
        classpath(GradlePlugin.FirebaseCrashlytics.get())
        classpath(GradlePlugin.Bintray.get())
        classpath(GradlePlugin.Hilt.get())
        classpath(GradlePlugin.Dokka.get())
    }
}

plugins {
    id("com.github.ben-manes.versions") version GradlePlugin.Versions.version
    id("io.github.gradle-nexus.publish-plugin") version GradlePlugin.NexusPublishPugin.version
}

apply(plugin = "org.jetbrains.dokka")

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks {
    val cleanBuild by registering(Delete::class) {
        delete(buildDir)
    }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {

    //disallow release candidates as upgradable versions from stable versions
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml").configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}


// Nexus Publish

extra["signing.keyId"] = ""
extra["signing.password"] = ""
extra["signing.key"] = ""
extra["ossrhUsername"] = ""
extra["ossrhPassword"] = ""
extra["sonatypeStagingProfileId"] = ""

val secretPropsFile = rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    // Read local.properties file first if it exists
    val properties = java.util.Properties()
    java.io.FileInputStream(secretPropsFile).use { file -> properties.load(file) }
    properties.forEach { name, value -> extra[name.toString()] = value }
} else {
    // Use system environment variables
    extra["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    extra["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    extra["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
    extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    extra["signing.password"] = System.getenv("SIGNING_PASSWORD")
    extra["signing.key"] = System.getenv("SIGNING_KEY")
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(extra["sonatypeStagingProfileId"].toString())
            username.set(extra["ossrhUsername"].toString())
            password.set(extra["ossrhPassword"].toString())
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}