// Top-level build file where you can add configuration options common to all sub-projects/modules.

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

    }
}

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

tasks.register("listrepos") {
    doLast {
        println("Repositories:")
        project.repositories.map{it as MavenArtifactRepository}
            .forEach{
                println("Name: ${it.name}; url: ${it.url}")
            }
    }
}
