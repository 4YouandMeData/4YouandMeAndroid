import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs")
    id("publish-mavencentral")
    id("dagger.hilt.android.plugin")
}

androidExtensions { isExperimental = true }

android {

    compileSdkVersion(ProjectConfig.compile_sdk)

    defaultConfig {
        minSdkVersion(ProjectConfig.min_sdk)
        targetSdkVersion(ProjectConfig.target_sdk)
        versionCode = ProjectConfig.version_code
        versionName = ProjectConfig.version_name
        testInstrumentationRunner = ProjectConfig.test_instrumentation_runner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    /* --- kotlin --- */

    implementation(Kotlin.StdLib.get())

    /* --- appcompact --- */

    implementation(AndroidX.AppCompat.get())

    /* --- fragment --- */

    implementation(AndroidX.FragmentKtx.get())

    /* --- core ktx --- */

    implementation(AndroidX.Core.CoreKtx.get())

    /* --- layout --- */

    implementation(AndroidX.ConstraintLayout.get())
    implementation(AndroidX.ViewPager2.get())
    implementation(AndroidX.SwipeRefreshLayout.get())

    /* --- android arch --- */

    api(AndroidX.Lifecycle.Service.get())
    implementation(AndroidX.Lifecycle.ViewModelKtx.get())
    implementation(AndroidX.Lifecycle.LiveDataKtx.get())
    implementation(AndroidX.Lifecycle.ViewModelSavedState.get())
    implementation(AndroidX.Lifecycle.CommonJava8.get())

    /* --- arrow --- */

    implementation(Arrow.Fx.get())
    implementation(Arrow.FxCoroutines.get())
    implementation(Arrow.Optics.get())
    implementation(Arrow.Syntax.get())
    kapt(Arrow.Meta.get())

    /* --- coroutines --- */

    implementation(KotlinX.Coroutines.Core.get())
    implementation(KotlinX.Coroutines.Android.get())

    /* --- navigation --- */

    implementation(AndroidX.Navigation.FragmentKtx.get())
    implementation(AndroidX.Navigation.UIKtx.get())

    /* --- encrypted prefs --- */

    implementation(AndroidX.Security.CryptoKtx.get())

    /* --- moshi --- */

    api(Squareup.Moshi.Kotlin.get())
    implementation(Squareup.Moshi.Adapters.get())

    /* --- json api --- */

    implementation(Banana.MoshiJsonApi.get())
    implementation(Banana.MoshiRetrofitConverter.get())

    /* --- timber --- */

    implementation(Jakewharton.Timber.get())

    /* --- retrofit --- */

    implementation(Squareup.Retrofit2.Retrofit.get())
    implementation(Squareup.Retrofit2.Converter.Moshi.get())
    implementation(Squareup.OkHttp3.LoggingInterceptor.get())

    /* --- country code picker --- */

    implementation(Hbb20.Ccp.get())

    /* --- span droid --- */

    implementation(GiacomoParisi.SpanDroid.Span.get())

    /* --- recycler view --- */

    implementation(GiacomoParisi.RecyclerDroid.Core.get())

    /* --- signature --- */

    implementation(GCacace.SignaturePad.get())

    /* --- threeten --- */

    implementation(Jakewharton.ThreeTenAbp.get())

    /* --- camera x --- */

    api(AndroidX.Camera.Core.get())
    api(AndroidX.Camera.Camera2.get())
    api(AndroidX.Camera.View.get())

    /* --- mp4 parser --- */

    implementation(Mp4Parser.IsoParser.get())

    /* --- toasty --- */

    implementation(GrenderG.Toasty.get())

    /* --- dexter --- */

    implementation(Karumi.Dexter.get())

    /* --- firebase --- */

    implementation(Google.Firebase.CrashlyticsKtx.get())
    implementation(Google.Firebase.AnalyticsKtx.get())
    implementation(Google.Firebase.MessagingKtx.get())

    /* --- lottie ---*/

    implementation(AirBnb.Lottie.get())

    /* --- chart ---*/

    implementation(PhilJay.MpAndroidChart.get())

    /* --- exo player --- */

    implementation(Google.ExoPlayer.Core.get())
    implementation(Google.ExoPlayer.UI.get())

    /* --- location --- */

    api(Google.PlayServices.Location.get())

    /* --- hilt --- */

    implementation(Google.Dagger.Hilt.Android.get())
    implementation(AndroidX.Hilt.LifecycleViewModel.get())
    kapt(AndroidX.Hilt.Compiler.get())
    kapt(Google.Dagger.Hilt.Compiler.get())

    /* --- room --- */
    api(AndroidX.Room.Runtime.get())
    api(AndroidX.Room.Ktx.get())
    kapt(AndroidX.Room.Compiler.get())

    /* --- test --- */

    testImplementation(Kotlin.Test.JUnit.get())
    testImplementation(IOMockK.MockK.get())
    testImplementation(Google.Test.Truth.get())
    testImplementation(AndroidX.ArchCore.Testing.get())
    testImplementation(KotlinX.Coroutines.Core.get())
    androidTestImplementation(AndroidX.Test.Ext.JunitKtx.get())
    androidTestImplementation(AndroidX.Test.Espresso.Core.get())

}
