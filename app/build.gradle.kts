plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
}

androidExtensions { isExperimental = true }

android {

    compileSdkVersion(AndroidConfig.compile_sdk)

    defaultConfig {
        applicationId = "com.foryouandme.app"
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)
        versionCode = AndroidConfig.version_code
        versionName = AndroidConfig.version_name
        testInstrumentationRunner = AndroidConfig.test_instrumentation_runner
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
}

dependencies {

    /* --- modules --- */

    implementation(project(":foryouandme"))

    /* --- kotlin --- */

    implementation(Kotlin.StdLib.get())

    /* --- android --- */

    implementation(AndroidX.AppCompat.get())
    implementation(AndroidX.Core.CoreKtx.get())

    /* --- firebase --- */

    implementation(Google.Firebase.CrashlyticsKtx.get())
    implementation(Google.Firebase.AnalyticsKtx.get())
    implementation(Google.Firebase.MessagingKtx.get())

    /* --- hilt --- */

    implementation(Google.Dagger.Hilt.Android.get())
    kapt(Google.Dagger.Hilt.Compiler.get())

    /* --- test --- */

    testImplementation(Kotlin.Test.JUnit.get())
    androidTestImplementation(AndroidX.Test.Ext.JunitKtx.get())
    androidTestImplementation(AndroidX.Test.Espresso.Core.get())

}
