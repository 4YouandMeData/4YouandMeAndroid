object GradlePlugin {

    object Android : Dependency(
        "com.android.tools.build",
        "gradle",
        "4.1.2"
    )

    object KotlinPlugin : Dependency(
        "org.jetbrains.kotlin",
        "kotlin-gradle-plugin",
        Kotlin.version
    )

    object GoogleServices : Dependency(
        "com.google.gms",
        "google-services",
        "4.3.4"
    )

    object NavigationSafeArgs :
        Dependency(
            "androidx.navigation",
            "navigation-safe-args-gradle-plugin",
            "2.2.2"
        )

    object FirebaseCrashlytics :
        Dependency(
            "com.google.firebase",
            "firebase-crashlytics-gradle",
            "2.4.1"
        )

    object Bintray : Dependency(
        "com.jfrog.bintray.gradle",
        "gradle-bintray-plugin",
        "1.8.5"
    )

    object Hilt :
        Dependency(
            "com.google.dagger",
            "hilt-android-gradle-plugin",
            Google.Dagger.Hilt.version
        )

   object Versions :
        Dependency(
            "com.github.ben-manes",
             "gradle-versions-plugin",
             "0.36.0"
        )

    object OneSignal:
        Dependency(
            "gradle.plugin.com.onesignal",
             "onesignal-gradle-plugin",
             "[0.12.9, 0.99.99]"
        )

}