package com.foryouandme.core.activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.*
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.ui.main.tasks.TasksError
import com.foryouandme.ui.main.tasks.TasksLoading
import com.foryouandme.ui.main.tasks.TasksStateUpdate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow

class FYAMViewModel @ViewModelInject constructor(
    private val stateUpdateFlow: StateUpdateFlow<FYAMStateUpdate>,
    private val loadingFlow: LoadingFlow<FYAMLoading>,
    private val errorFlow: ErrorFlow<FYAMError>,
    private val getConfigurationUseCase: GetConfigurationUseCase
) : ViewModel() {

    /* --- state --- */

    var state = FYAMState()
        private set

    /* --- flow --- */

    val stateUpdate: SharedFlow<FYAMStateUpdate> = stateUpdateFlow.stateUpdates
    val loading: SharedFlow<UILoading<FYAMLoading>> = loadingFlow.loading
    val error: SharedFlow<UIError<FYAMError>> = errorFlow.error

    /* --- initialize --- */

    private suspend fun initialize(
        taskId: String?,
        url: String?,
        openAppIntegration: String?,
        splashLoading: Boolean = false
    ) {

        val configuration =
            if (splashLoading) getConfigurationWithSplash()
            else getConfiguration()


        val initializedState =
            FYAMState(
                configuration,
                taskId?.toEvent(),
                url?.toEvent(),
                openAppIntegration?.let { IntegrationApp.fromIdentifier(it) }?.toEvent()
            )

        state = initializedState
        stateUpdateFlow.update(FYAMStateUpdate.Config(configuration))

        loadingFlow.hide(FYAMLoading.Config)
        loadingFlow.hide(FYAMLoading.Splash)

    }

    /* --- configuration --- */

    private suspend fun getConfigurationWithSplash(): Configuration =
        coroutineScope {

            loadingFlow.show(FYAMLoading.Splash)

            val animationDelay =
                async {
                    delay(1000)
                    loadingFlow.hide(FYAMLoading.Splash)
                    loadingFlow.show(FYAMLoading.Config)
                }
            val configuration = async { getConfigurationUseCase(Policy.LocalFirst) }

            animationDelay.await()
            configuration.await()

        }

    private suspend fun getConfiguration(): Configuration {

        loadingFlow.show(FYAMLoading.Config)

        return getConfigurationUseCase(Policy.LocalFirst)

    }

    /* --- state event --- */

    fun execute(stateEvent: FYAMStateEvent) {

        when (stateEvent) {
            is FYAMStateEvent.Initialize ->
                errorFlow.launchCatch(viewModelScope, FYAMError.Config)
                {
                    initialize(
                        stateEvent.taskId,
                        stateEvent.url,
                        stateEvent.openAppIntegration,
                        stateEvent.splashLoading
                    )
                }
        }

    }

}