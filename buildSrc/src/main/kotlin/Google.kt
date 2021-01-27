object Google {

    object Material : Dependency(
        "com.google.android.material",
        "material",
        "1.2.1"
    )

    object Firebase : DependencyIndependentGroup("com.google.firebase") {

        object CrashlyticsKtx : Dependency(
            Firebase.group,
            "firebase-crashlytics-ktx",
            "17.3.0"
        )

        object AnalyticsKtx : Dependency(
            Firebase.group,
            "firebase-analytics-ktx",
            "18.0.1"
        )

        object MessagingKtx : Dependency(
            Firebase.group,
            "firebase-messaging-ktx",
            "21.0.1"
        )

    }

    object ExoPlayer : DependencyGroup("com.google.android.exoplayer", "2.12.3") {

        object Core : Dependency(ExoPlayer.group, "exoplayer-core", ExoPlayer.version)

        object UI : Dependency(ExoPlayer.group, "exoplayer-ui", ExoPlayer.version)

    }

    object Dagger : DependencyIndependentGroup("com.google.dagger") {

        object Hilt : DependencyGroup(Dagger.group, "2.31.2-alpha") {

            object Android : Dependency(Dagger.group, "hilt-android", Hilt.version)

            object Compiler : Dependency(
                Dagger.group,
                "hilt-android-compiler",
                Hilt.version
            )

        }

    }

    object Test {

        object Truth : Dependency("com.google.truth", "truth", "1.1.2")

    }

}