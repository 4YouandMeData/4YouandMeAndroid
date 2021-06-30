object GradlePlugin {

    object Android : Dependency(
        "com.android.tools.build",
        "gradle",
        "7.1.0-alpha02"
    )

    object KotlinPlugin : Dependency(
        "org.jetbrains.kotlin",
        "kotlin-gradle-plugin",
        Kotlin.version
    )

    object GoogleServices : Dependency(
        "com.google.gms",
        "google-services",
        "4.3.8"
    )

    object NavigationSafeArgs :
        Dependency(
            "androidx.navigation",
            "navigation-safe-args-gradle-plugin",
            AndroidX.Navigation.version
        )

    object FirebaseCrashlytics :
        Dependency(
            "com.google.firebase",
            "firebase-crashlytics-gradle",
            "2.7.1"
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
             "0.38.0"
        )

    object OneSignal:
        Dependency(
            "gradle.plugin.com.onesignal",
             "onesignal-gradle-plugin",
             "[0.12.9, 0.99.99]"
        )

    object Dokka:
            Dependency(
                "org.jetbrains.dokka",
                "dokka-gradle-plugin",
                "1.4.32"
            )

}