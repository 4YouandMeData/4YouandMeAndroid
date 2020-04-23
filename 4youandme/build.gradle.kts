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

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("android.arch.lifecycle:extensions:1.1.1")

    /* --- arrow --- */

    implementation("io.arrow-kt:arrow-fx:0.10.5")
    implementation("io.arrow-kt:arrow-optics:0.10.5")
    implementation("io.arrow-kt:arrow-syntax:0.10.5")
    kapt("io.arrow-kt:arrow-meta:0.10.5")

    /* coroutines */

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

    /* --- navigation --- */

    implementation ("androidx.navigation:navigation-fragment-ktx:2.2.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.2.2")

    /* --- test --- */

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)
}
