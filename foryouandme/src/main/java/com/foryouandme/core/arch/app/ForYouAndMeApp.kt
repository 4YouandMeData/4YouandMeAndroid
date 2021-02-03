package com.foryouandme.core.arch.app

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.foryouandme.BuildConfig
import com.foryouandme.core.arch.android.AppInjector
import com.foryouandme.core.arch.deps.ForYouAndMeInjector
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.Injector
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.foryouandme.data.datasource.Environment
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber
import timber.log.Timber.DebugTree

abstract class ForYouAndMeApp : Application(), AppInjector, CameraXConfig.Provider {

    abstract val environment: Environment

    abstract val imageConfiguration: ImageConfiguration

    abstract val videoConfiguration: VideoConfiguration

    abstract val firebaseAnalytics: FirebaseAnalytics

    override val injector: Injector by lazy {
        ForYouAndMeInjector(
            this,
            environment,
            imageConfiguration,
            videoConfiguration,
            firebaseAnalytics
        )
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())

        AndroidThreeTen.init(this)
    }

    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()

}