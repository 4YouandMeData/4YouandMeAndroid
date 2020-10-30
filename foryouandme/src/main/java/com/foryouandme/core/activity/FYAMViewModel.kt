package com.foryouandme.core.activity

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.ConfigurationModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.foryouandme.core.entity.integration.IntegrationApp

class FYAMViewModel(
    navigator: Navigator,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<FYAMState, FYAMStateUpdate, FYAMError, FYAMLoading>(navigator) {

    suspend fun initialize(
        rootNavController: RootNavController,
        taskId: String?,
        url: String?,
        openAppIntegration: String?
    ): Either<ForYouAndMeError, FYAMState> {

        showLoading(FYAMLoading.Config)

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)
                .handleAuthError(rootNavController, navigator)

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

        return fyamState

    }

}