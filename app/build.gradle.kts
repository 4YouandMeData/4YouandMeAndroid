plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
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

    implementation(Kotlin.stdlib.jdk7)

    /* --- android --- */

    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)

    /* --- firebase --- */

    implementation(Firebase.crashlyticsKtx)
    implementation(Firebase.analyticsKtx)
    implementation(Firebase.messagingKtx)

    /* --- test --- */

    testImplementation(Kotlin.Test.junit)
    androidTestImplementation(AndroidX.Test.ext.junitKtx)
    androidTestImplementation(AndroidX.Test.espresso.core)

}
