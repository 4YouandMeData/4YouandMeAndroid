package org.fouryouandme.aboutyou

import androidx.navigation.NavController
import org.fouryouandme.core.arch.navigation.Navigator
import arrow.fx.ForIO
import org.fouryouandme.aboutyou.AboutYouError
import org.fouryouandme.aboutyou.AboutYouLoading
import org.fouryouandme.aboutyou.AboutYouState
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.ParentNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.ext.unsafeRunAsync

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
        parentNavController: ParentNavController,
        aboutYouNavController: AboutYouNavController
    ): Unit {
        if (navigator.back(parentNavController).not())
            navigator.back(aboutYouNavController)
    }
}