plugins {
    `kotlin-dsl`
}

buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("com.android.tools.build:gradle:7.0.0-alpha12")
    }

}

repositories {
    google()
    jcenter()
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.32")
    // Android gradle plugin will allow us to access Android specific features
    implementation("com.android.tools.build:gradle:7.0.0-alpha12")

}