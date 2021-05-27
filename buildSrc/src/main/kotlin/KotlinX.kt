object KotlinX : DependencyGroup("org.jetbrains.kotlinx", "1.5.0") {

    object Coroutines {

        object Core : Dependency(
            KotlinX.group,
            "kotlinx-coroutines-core",
            KotlinX.version
        )

        object Android : Dependency(
            KotlinX.group,
            "kotlinx-coroutines-android",
            KotlinX.version
        )

    }

}