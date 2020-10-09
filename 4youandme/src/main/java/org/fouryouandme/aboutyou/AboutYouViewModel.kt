package org.fouryouandme.aboutyou

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
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
        AboutYouState,
        AboutYouStateUpdate,
        AboutYouError,
        AboutYouLoading>
    (
    navigator = navigator,
    runtime = runtime
) {

    /* --- data --- */

    suspend fun initialize(): Unit {

        //TODO: handle AuthError
        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)

        configuration.fold(
            { setErrorFx(it, AboutYouError.Initialization) },
            {
                setStateFx(
                    AboutYouState(it),
                ) { state ->
                    AboutYouStateUpdate.Initialization(state.configuration)
                }
            }
        )
    }

    /* --- navigation --- */

    suspend fun back(
        aboutYouNavController: AboutYouNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(aboutYouNavController).not())
            navigator.back(rootNavController)
    }
}