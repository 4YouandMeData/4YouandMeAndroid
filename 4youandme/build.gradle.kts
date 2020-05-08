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
    implementation("io.arrow-kt:arrow-integration-retrofit-adapter:0.10.4")
    kapt(Libs.arrow_meta)

    /* coroutines */

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    /* --- navigation --- */

    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    /* --- encrypted prefs --- */

    implementation("androidx.security:security-crypto:1.0.0-rc01")

    /* --- moshi --- */

    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")

    /* --- timber --- */

    implementation("com.jakewharton.timber:timber:4.7.1")

    /* --- retrofit --- */

    implementation("com.squareup.retrofit2:retrofit:2.8.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.8.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.1")

    /* --- test --- */

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)
}
