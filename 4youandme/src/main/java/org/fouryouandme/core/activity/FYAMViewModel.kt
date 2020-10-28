package org.fouryouandme.core.activity

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.entity.integration.IntegrationApp

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