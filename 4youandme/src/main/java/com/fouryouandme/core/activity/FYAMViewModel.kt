package com.fouryouandme.core.activity

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.deps.modules.ConfigurationModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.arch.error.handleAuthError
import com.fouryouandme.core.arch.livedata.toEvent
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.fouryouandme.core.entity.integration.IntegrationApp

class FYAMViewModel(
    navigator: Navigator,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<FYAMState, FYAMStateUpdate, FYAMError, FYAMLoading>(navigator) {

    suspend fun initialize(
        rootNavController: RootNavController,
        taskId: String?,
        url: String?,
        openAppIntegration: String?
    ): Either<FourYouAndMeError, FYAMState> {

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