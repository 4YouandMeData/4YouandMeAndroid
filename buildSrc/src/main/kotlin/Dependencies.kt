open class DependencyGroup(val group: String, val version: String)

open class DependencyIndependentGroup(val group: String)

open class Dependency(
    private val group: String,
    private val artifact: String,
    private val version: String
) {

    fun get(): String = "$group:$artifact:$version"

}

object Arrow : DependencyGroup("io.arrow-kt", "0.11.0") {

    object Fx : Dependency(Arrow.group, "arrow-fx", Arrow.version)

    object FxCoroutines : Dependency(Arrow.group, "arrow-fx-coroutines", Arrow.version)

    object Optics : Dependency(Arrow.group, "arrow-optics", Arrow.version)

    object Syntax : Dependency(Arrow.group, "arrow-syntax", Arrow.version)

    object Meta : Dependency(Arrow.group, "arrow-meta", Arrow.version)

}

object Squareup {

    object Moshi : DependencyGroup("com.squareup.moshi", "1.11.0") {

        object Kotlin : Dependency(Moshi.group, "moshi-kotlin", Moshi.version)

        object Adapters : Dependency(Moshi.group, "moshi-adapters", Moshi.version)

    }

    object Retrofit2 : DependencyGroup("com.squareup.retrofit2", "2.9.0") {

        object Retrofit : Dependency(Retrofit2.group, "retrofit", Retrofit2.version)

        object Converter {

            object Moshi : Dependency(Retrofit2.group, "converter-moshi", Retrofit2.version)

        }

    }

    object OkHttp3 : DependencyGroup("com.squareup.okhttp3", "4.9.0") {

        object LoggingInterceptor :
            Dependency(OkHttp3.group, "logging-interceptor", OkHttp3.version)

    }

}

object Banana : DependencyGroup("moe.banana", "3.5.0") {

    object MoshiJsonApi : Dependency(Banana.group, "moshi-jsonapi", Banana.version)

    object MoshiRetrofitConverter :
        Dependency(Banana.group, "moshi-jsonapi-retrofit-converter", Banana.version)

}

object Jakewharton : DependencyGroup("com.jakewharton", "4.7.1") {

    object Timber : Dependency("${Jakewharton.group}.timber", "timber", Jakewharton.version)

    object ThreeTenAbp : Dependency("${Jakewharton.group}.threetenabp", "threetenabp", "1.3.0")

}

object Hbb20 : DependencyGroup("com.hbb20", "2.4.1") {

    object Ccp : Dependency(Hbb20.group, "ccp", Hbb20.version)

}

object GiacomoParisi {

    object SpanDroid : DependencyGroup("com.giacomoparisi.spandroid", "0.1") {

        object Span : Dependency(SpanDroid.group, "span-droid", SpanDroid.version)

    }

    object RecyclerDroid : DependencyGroup("com.giacomoparisi.recyclerdroid", "1.8.0-beta07") {

        object Core : Dependency(RecyclerDroid.group, "recycler-droid-core", RecyclerDroid.version)

    }

}

object Mp4Parser : DependencyGroup("com.googlecode.mp4parser", "1.1.22") {

    object IsoParser : Dependency(Mp4Parser.group, "isoparser", Mp4Parser.version)

}

object GrenderG : DependencyGroup("com.github.GrenderG", "1.5.0") {

    object Toasty : Dependency(GrenderG.group, "Toasty", GrenderG.version)

}

object Karumi : DependencyGroup("com.karumi", "6.2.1") {

    object Dexter : Dependency(Karumi.group, "dexter", Karumi.version)

}

object GCacace : DependencyGroup("com.github.gcacace", "1.3.1") {

    object SignaturePad : Dependency(GCacace.group, "signature-pad", GCacace.version)

}

object AirBnb : DependencyGroup("com.airbnb.android", "3.5.0") {

    object Lottie : Dependency(AirBnb.group, "lottie", AirBnb.version)

}

object PhilJay : DependencyGroup("com.github.PhilJay", "3.1.0") {

    object MpAndroidChart : Dependency(PhilJay.group, "MPAndroidChart", PhilJay.version)

}

object IOMockK : DependencyGroup("io.mockk", "1.10.2") {

    object MockK : Dependency(IOMockK.group, "mockk", IOMockK.version)

}

object Google {

    object Material : Dependency("com.google.android.material", "material", "1.2.1")

    object Firebase : DependencyIndependentGroup("com.google.firebase") {

        object CrashlyticsKtx : Dependency(Firebase.group, "firebase-crashlytics-ktx", "17.3.0")

        object AnalyticsKtx : Dependency(Firebase.group, "firebase-analytics-ktx", "18.0.0")

        object MessagingKtx : Dependency(Firebase.group, "firebase-messaging-ktx", "21.0.0")

    }

    object ExoPlayer : DependencyGroup("com.google.android.exoplayer", "2.12.1") {

        object Core : Dependency(ExoPlayer.group, "exoplayer-core", ExoPlayer.version)

        object UI : Dependency(ExoPlayer.group, "exoplayer-ui", ExoPlayer.version)

    }

    object Test {

        object Truth : Dependency("com.google.truth", "truth", "1.1")

    }

}

object Kotlin : DependencyGroup("org.jetbrains.kotlin", "1.4.20") {

    object StdLib : Dependency(Kotlin.group, "kotlin-stdlib", Kotlin.version)

    object Test {

        object JUnit : Dependency(Kotlin.group, "kotlin-test-junit", Kotlin.version)

    }

}

object KotlinX : DependencyGroup("org.jetbrains.kotlinx", "1.4.1") {

    object Coroutines {

        object Core : Dependency(KotlinX.group, "kotlinx-coroutines-core", KotlinX.version)

        object Android : Dependency(KotlinX.group, "kotlinx-coroutines-android", KotlinX.version)

    }

}

object AndroidX {

    object AppCompat : Dependency("androidx.appcompat", "appcompat", "1.2.0")

    object RecyclerView : Dependency("androidx.recyclerview", "recyclerview", "1.1.0")

    object FragmentKtx : Dependency("androidx.fragment", "fragment-ktx", "1.2.5")

    object ViewPager2 : Dependency("androidx.viewpager2", "viewpager2", "1.0.0")

    object SwipeRefreshLayout :
        Dependency("androidx.swiperefreshlayout", "swiperefreshlayout", "1.1.0")

    object Core : DependencyGroup("androidx.core", "1.3.2") {

        object CoreKtx : Dependency(Core.group, "core-ktx", Core.version)

    }

    object ConstraintLayout : Dependency("androidx.constraintlayout", "constraintlayout", "2.0.4")

    object Paging : DependencyGroup("androidx.paging", "3.0.0-alpha09") {

        object Runtime : Dependency(Paging.group, "paging-runtime", Paging.version)

    }

    object Navigation : DependencyGroup("androidx.navigation", "2.2.2") {

        object FragmentKtx : Dependency(Navigation.group, "navigation-fragment-ktx", Navigation.version)

        object UIKtx : Dependency(Navigation.group, "navigation-ui-ktx", Navigation.version)

    }

    object Lifecycle : DependencyGroup("androidx.lifecycle", "2.2.0") {

        object ViewModelKtx : Dependency(Lifecycle.group, "lifecycle-viewmodel-ktx", Lifecycle.version)

        object ViewModelSavedState : Dependency(Lifecycle.group, "lifecycle-viewmodel-savedstate", Lifecycle.version)

        object LiveDataKtx : Dependency(Lifecycle.group, "lifecycle-livedata-ktx", Lifecycle.version)

        object Service : Dependency(Lifecycle.group, "lifecycle-service", Lifecycle.version)

    }

    object Security : DependencyGroup("androidx.security", "1.1.0-alpha02") {

        object CryptoKtx : Dependency(Security.group, "security-crypto-ktx", Security.version)

    }

    object Camera : DependencyIndependentGroup("androidx.camera") {

        object Core : Dependency(Camera.group, "camera-core", "1.0.0-beta12")

        object Camera2 : Dependency(Camera.group, "camera-camera2", "1.0.0-beta12")

        object View : Dependency(Camera.group, "camera-view", "1.0.0-alpha19")

    }

    object ArchCore : DependencyGroup("androidx.arch.core", "2.1.0") {

        object Testing : Dependency(ArchCore.group, "core-testing", ArchCore.version)

    }

    object Test : DependencyIndependentGroup("androidx.test") {

        object Ext : DependencyGroup("${Test.group}.ext", "1.1.2") {

            object JunitKtx : Dependency(Ext.group, "junit-ktx", Ext.version)

        }

        object Espresso : DependencyGroup("$group.espresso", "3.3.0") {

            object Core : Dependency(Espresso.group, "espresso-core", Espresso.version)

        }

    }

}

object GradlePlugin {

    object Android : Dependency("com.android.tools.build", "gradle", "4.1.1")

    object KotlinPlugin : Dependency("org.jetbrains.kotlin", "kotlin-gradle-plugin", Kotlin.version)

    object GoogleServices : Dependency("com.google.gms", "google-services", "4.3.4")

    object NavigationSafeArgs :
        Dependency("androidx.navigation", "navigation-safe-args-gradle-plugin", "2.2.2")

    object FirebaseCrashlytics :
        Dependency("com.google.firebase", "firebase-crashlytics-gradle", "2.4.1")

    object Bintray : Dependency("com.jfrog.bintray.gradle", "gradle-bintray-plugin", "1.8.5")

}
