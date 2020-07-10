// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Libs.com_android_tools_build_gradle)
        classpath(Libs.kotlin_gradle_plugin)
        classpath(Libs.google_services)
        classpath(Libs.navigation_safe_args_gradle_plugin)
    }
}

plugins {
    id("de.fayard.buildSrcVersions") version "0.7.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
        maven("https://dl.bintray.com/giacomoparisi/span-droid")
        maven("https://dl.bintray.com/giacomoparisi/recycler-droid")
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
