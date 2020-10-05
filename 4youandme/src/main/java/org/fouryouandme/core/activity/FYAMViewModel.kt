package org.fouryouandme.core.activity

import androidx.lifecycle.SavedStateHandle
import arrow.core.Either
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseSaveStateViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.entity.configuration.Configuration

class FYAMViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val stateHandle: SavedStateHandle,
    private val configurationModule: ConfigurationModule
) : BaseSaveStateViewModel<ForIO, FYAMState, FYAMStateUpdate, FYAMError, FYAMLoading>
    (savedStateHandle = stateHandle, navigator = navigator, runtime = runtime) {

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<FourYouAndMeError, Configuration> {

        showLoadingFx(FYAMLoading.Config)

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)
                .handleAuthError(rootNavController, navigator)

        configuration.fold(
            {
                setErrorFx(it, FYAMError.Config)
            },
            { config ->

                setStateFx(FYAMState(config))
                { FYAMStateUpdate.Config(it.configuration) }

            }
        )


        hideLoadingFx(FYAMLoading.Config)

        return configuration

    }

}