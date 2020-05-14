plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

androidExtensions { isExperimental = true }

android {

    compileSdkVersion(AndroidConfig.compile_sdk)

    defaultConfig {
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)
        versionCode = AndroidConfig.version_code
        versionName = AndroidConfig.version_name
        testInstrumentationRunner = AndroidConfig.test_instrumentation_runner
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
}

dependencies {

    /* --- kotlin --- */

    implementation(Libs.kotlin_stdlib_jdk7)

    /* --- appcompact --- */

    implementation(Libs.appcompat)

    /* --- core ktx --- */

    implementation(Libs.core_ktx)

    /* --- layout --- */

    implementation(Libs.constraintlayout)

    /* --- android arch --- */

    implementation(Libs.lifecycle_viewmodel_ktx)
    implementation(Libs.lifecycle_livedata_ktx)
    implementation(Libs.android_arch_lifecycle_extensions)

    /* --- arrow --- */

    implementation(Libs.arrow_fx)
    implementation(Libs.arrow_optics)
    implementation(Libs.arrow_syntax)
    implementation(Libs.arrow_integration_retrofit_adapter)
    kapt(Libs.arrow_meta)

    /* coroutines */

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    /* --- navigation --- */

    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    /* --- encrypted prefs --- */

    implementation(Libs.security_crypto)

    /* --- moshi --- */

    implementation(Libs.moshi_kotlin)

    /* --- timber --- */

    implementation(Libs.timber)

    /* --- retrofit --- */

    implementation(Libs.retrofit)
    implementation(Libs.converter_moshi)
    implementation(Libs.logging_interceptor)

    /* --- country code picker --- */

    implementation(Libs.ccp)

    /* --- test --- */

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)
}
