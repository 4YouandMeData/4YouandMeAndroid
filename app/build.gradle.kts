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
        applicationId = "org.fouryouandme.app"
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

    implementation(project(":4youandme"))

    /* --- kotlin --- */

    implementation(Libs.kotlin_stdlib_jdk7)

    /* --- android --- */

    implementation(Libs.appcompat)
    implementation(Libs.core_ktx)

    /* --- firebase --- */

    implementation(Libs.firebase_analytics_ktx)
    implementation(Libs.firebase_crashlytics_ktx)

    /* --- test --- */

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)
}
