package org.fouryouandme.aboutyou

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration

class AboutYouViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<
        ForIO,
        Empty,
        Empty,
        Empty,
        Empty>
    (
    Empty,
    navigator = navigator,
    runtime = runtime
) {

    /* --- navigation --- */

    suspend fun back(
        aboutYouNavController: AboutYouNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(aboutYouNavController).not())
            navigator.back(rootNavController)
    }
}