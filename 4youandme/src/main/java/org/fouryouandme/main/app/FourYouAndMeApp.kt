package org.fouryouandme.main.app

import android.app.Application
import org.fouryouandme.core.arch.android.AppInjector
import org.fouryouandme.core.arch.deps.Injector

class FourYouAndMeApp : Application(), AppInjector {

    override val injector: Injector by lazy { ForYouAndMeInjector(this) }
}