package org.fouryouandme.core.arch.app

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.jakewharton.threetenabp.AndroidThreeTen
import org.fouryouandme.BuildConfig
import org.fouryouandme.core.arch.android.AppInjector
import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.arch.deps.ForYouAndMeInjector
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Injector
import org.fouryouandme.researchkit.task.TaskConfiguration
import org.fouryouandme.researchkit.task.TaskInjector
import timber.log.Timber
import timber.log.Timber.DebugTree


abstract class FourYouAndMeApp : Application(), AppInjector, TaskInjector, CameraXConfig.Provider {

    abstract val environment: Environment

    abstract val imageConfiguration: ImageConfiguration

    override val injector: Injector by lazy {
        ForYouAndMeInjector(
            this,
            environment,
            imageConfiguration
        )
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())

        AndroidThreeTen.init(this)
    }

    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()

    override fun provideBuilder(): TaskConfiguration = injector.taskConfiguration
}