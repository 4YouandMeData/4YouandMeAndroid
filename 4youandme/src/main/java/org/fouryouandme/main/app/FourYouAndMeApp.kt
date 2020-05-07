package org.fouryouandme.main.app

import android.app.Application
import org.fouryouandme.BuildConfig
import org.fouryouandme.core.arch.android.AppInjector
import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Injector
import timber.log.Timber
import timber.log.Timber.DebugTree


abstract class FourYouAndMeApp : Application(), AppInjector {

    abstract val environment: Environment

    abstract val imageConfiguration: ImageConfiguration

    override val injector: Injector by lazy {
        ForYouAndMeInjector(this, environment, imageConfiguration)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
    }
}