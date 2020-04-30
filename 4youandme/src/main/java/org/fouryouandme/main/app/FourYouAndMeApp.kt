package org.fouryouandme.main.app

import android.app.Application
import org.fouryouandme.BuildConfig
import org.fouryouandme.core.arch.android.AppInjector
import org.fouryouandme.core.arch.deps.Injector
import timber.log.Timber
import timber.log.Timber.DebugTree


class FourYouAndMeApp : Application(), AppInjector {

    override val injector: Injector by lazy { ForYouAndMeInjector(this) }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
    }
}