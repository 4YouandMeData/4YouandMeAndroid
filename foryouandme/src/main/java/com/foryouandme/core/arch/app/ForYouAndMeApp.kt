package com.foryouandme.core.arch.app

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.foryouandme.BuildConfig
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.foryouandme.data.datasource.Environment
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber
import timber.log.Timber.DebugTree

abstract class ForYouAndMeApp : Application(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())

        AndroidThreeTen.init(this)
    }

    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()

}