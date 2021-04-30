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

    compileSdk = ProjectConfig.compile_sdk

    defaultConfig {
        applicationId = "com.foryouandme.app"
        minSdk = ProjectConfig.min_sdk
        targetSdk = ProjectConfig.target_sdk
        versionCode = ProjectConfig.version_code
        versionName = ProjectConfig.version_name
        testInstrumentationRunner = ProjectConfig.test_instrumentation_runner
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
        jvmTarget = "1.8"
        useIR = true
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
    implementation(AndroidX.Hilt.LifecycleViewModel.get())
    kapt(AndroidX.Hilt.Compiler.get())
    kapt(Google.Dagger.Hilt.Compiler.get())

    /* --- test --- */

    testImplementation(Kotlin.Test.JUnit.get())
    androidTestImplementation(AndroidX.Test.Ext.JunitKtx.get())
    androidTestImplementation(AndroidX.Test.Espresso.Core.get())

}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}