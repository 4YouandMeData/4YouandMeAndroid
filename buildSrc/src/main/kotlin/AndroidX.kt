object AndroidX {

    object AppCompat : Dependency(
        "androidx.appcompat",
        "appcompat",
        "1.2.0"
    )

    object RecyclerView : Dependency(
        "androidx.recyclerview",
        "recyclerview",
        "1.1.0"
    )

    object FragmentKtx : Dependency(
        "androidx.fragment",
        "fragment-ktx",
        "1.2.5"
    )

    object ViewPager2 : Dependency(
        "androidx.viewpager2",
        "viewpager2",
        "1.0.0"
    )

    object SwipeRefreshLayout :
        Dependency(
            "androidx.swiperefreshlayout",
            "swiperefreshlayout",
            "1.1.0"
        )

    object Core : DependencyGroup("androidx.core", "1.3.2") {

        object CoreKtx : Dependency(Core.group, "core-ktx", Core.version)

    }

    object ConstraintLayout : Dependency(
        "androidx.constraintlayout",
        "constraintlayout",
        "2.0.4"
    )

    object Paging : DependencyGroup("androidx.paging", "3.0.0-alpha09") {

        object Runtime : Dependency(Paging.group, "paging-runtime", Paging.version)

    }

    object Navigation : DependencyGroup("androidx.navigation", "2.2.2") {

        object FragmentKtx :
            Dependency(Navigation.group, "navigation-fragment-ktx", Navigation.version)

        object UIKtx : Dependency(Navigation.group, "navigation-ui-ktx", Navigation.version)

    }

    object Lifecycle : DependencyGroup("androidx.lifecycle", "2.2.0") {

        object ViewModelKtx :
            Dependency(Lifecycle.group, "lifecycle-viewmodel-ktx", Lifecycle.version)

        object ViewModelSavedState :
            Dependency(
                Lifecycle.group,
                "lifecycle-viewmodel-savedstate",
                Lifecycle.version
            )

        object LiveDataKtx :
            Dependency(Lifecycle.group, "lifecycle-livedata-ktx", Lifecycle.version)

        object Service : Dependency(
            Lifecycle.group,
            "lifecycle-service",
            Lifecycle.version
        )

        object CommonJava8 : Dependency(
            Lifecycle.group,
            "lifecycle-common-java8",
            Lifecycle.version
        )

    }

    object Security : DependencyGroup("androidx.security", "1.1.0-alpha02") {

        object CryptoKtx : Dependency(
            Security.group,
            "security-crypto-ktx",
            Security.version
        )

    }

    object Camera : DependencyIndependentGroup("androidx.camera") {

        object Core : Dependency(Camera.group, "camera-core", "1.0.0-rc01")

        object Camera2 : Dependency(Camera.group, "camera-camera2", "1.0.0-rc01")

        object View : Dependency(Camera.group, "camera-view", "1.0.0-alpha20")

    }

    object ArchCore : DependencyGroup("androidx.arch.core", "2.1.0") {

        object Testing : Dependency(ArchCore.group, "core-testing", ArchCore.version)

    }

    object Hilt : DependencyGroup("androidx.hilt", "1.0.0-alpha02") {

        object LifecycleViewModel : Dependency(
            Hilt.group,
            "hilt-lifecycle-viewmodel",
            Hilt.version
        )

        object Compiler : Dependency(Hilt.group, "hilt-compiler", Hilt.version)

    }

    object Room : DependencyGroup("androidx.room", "2.2.6") {

        object Runtime : Dependency(Room.group, "room-runtime", Room.version)

        object Ktx : Dependency(Room.group, "room-ktx", Room.version)

        object Compiler : Dependency(Room.group, "room-compiler", Room.version)

    }

    object Test : DependencyIndependentGroup("androidx.test") {

        object Ext : DependencyGroup("${Test.group}.ext", "1.1.2") {

            object JunitKtx : Dependency(Ext.group, "junit-ktx", Ext.version)

        }

        object Espresso : DependencyGroup("$group.espresso", "3.3.0") {

            object Core : Dependency(Espresso.group, "espresso-core", Espresso.version)

        }

        object Core : Dependency(Test.group, "core", "1.3.0")

        object Rules : Dependency(Test.group, "rules", "1.1.0")

        object Runner : Dependency(Test.group, "runner", "1.1.0")

    }
}