package org.fouryouandme.core.activity

import arrow.core.Either
import org.fouryouandme.core.arch.android.BaseViewModel
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
    private val configurationModule: ConfigurationModule
) : BaseViewModel<FYAMState, FYAMStateUpdate, FYAMError, FYAMLoading>(navigator) {

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<FourYouAndMeError, Configuration> {

        showLoading(FYAMLoading.Config)

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)
                .handleAuthError(rootNavController, navigator)

        configuration.fold(
            {
                setError(it, FYAMError.Config)
            },
            { config ->

                setState(FYAMState(config))
                { FYAMStateUpdate.Config(it.configuration) }

            }
        )

        hideLoading(FYAMLoading.Config)

        return configuration

    }

}