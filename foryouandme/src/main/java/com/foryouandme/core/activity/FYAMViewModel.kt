package com.foryouandme.core.activity

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parMapN
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.ConfigurationModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.integration.IntegrationApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class FYAMViewModel(
    navigator: Navigator,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<FYAMState, FYAMStateUpdate, FYAMError, FYAMLoading>(navigator) {

    suspend fun initialize(
        rootNavController: RootNavController,
        taskId: String?,
        url: String?,
        openAppIntegration: String?,
        splashLoading: Boolean = false
    ): Either<ForYouAndMeError, FYAMState> {

        val configuration =
            if (splashLoading) getConfigurationWithSplash(rootNavController)
            else (getConfiguration(rootNavController))

        val fyamState =
            configuration.fold(
                {
                    setError(it, FYAMError.Config)
                    it.left()
                },
                { config ->

                    val state =
                        FYAMState(
                            config,
                            taskId?.toEvent(),
                            url?.toEvent(),
                            openAppIntegration?.let { IntegrationApp.fromIdentifier(it) }?.toEvent()
                        )

                    setState(state)
                    { FYAMStateUpdate.Config(it.configuration) }

                    state.right()

                }
            )

        hideLoading(FYAMLoading.Config)
        hideLoading(FYAMLoading.Splash)

        return fyamState

    }

    private suspend fun getConfigurationWithSplash(
        rootNavController: RootNavController
    ): Either<ForYouAndMeError, Configuration> {

        showLoading(FYAMLoading.Splash)

        return parMapN(
            Dispatchers.IO,
            suspend {
                delay(1000)
                hideLoading(FYAMLoading.Splash)
                showLoading(FYAMLoading.Config)
            },
            suspend {
                configurationModule.getConfiguration(CachePolicy.MemoryFirst)
                    .handleAuthError(rootNavController, navigator)
            },
            { _, config -> config }
        )
    }

    private suspend fun getConfiguration(
        rootNavController: RootNavController
    ): Either<ForYouAndMeError, Configuration> {

        showLoading(FYAMLoading.Config)

        return configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .handleAuthError(rootNavController, navigator)


    }


}