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

    }
}

apply(plugin = "com.github.ben-manes.versions")

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
    val clean by registering(Delete::class) {
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

tasks.register("listrepos") {
    doLast {
        println("Repositories:")
        project.repositories.map{it as MavenArtifactRepository}
            .forEach{
                println("Name: ${it.name}; url: ${it.url}")
            }
    }
}
