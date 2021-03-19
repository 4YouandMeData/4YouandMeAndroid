// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {

    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
        maven("https://dl.bintray.com/giacomoparisi/span-droid")
        maven("https://dl.bintray.com/giacomoparisi/recycler-droid")
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
        classpath(GradlePlugin.Versions.get())
        classpath(GradlePlugin.Dokka.get())
    }
}

apply(plugin = "com.github.ben-manes.versions")
apply(plugin = "org.jetbrains.dokka")

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
        maven("https://dl.bintray.com/giacomoparisi/span-droid")
        maven("https://dl.bintray.com/giacomoparisi/recycler-droid")
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