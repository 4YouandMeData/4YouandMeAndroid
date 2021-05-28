package com.foryouandme.core.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.device.SendDeviceInfoUseCase
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.integration.IntegrationApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class FYAMViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<FYAMStateUpdate>,
    private val loadingFlow: LoadingFlow<FYAMLoading>,
    private val errorFlow: ErrorFlow<FYAMError>,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendDeviceInfoUseCase: SendDeviceInfoUseCase,
) : ViewModel() {

    /* --- state --- */

    var state = FYAMState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

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

        // try to refresh configuration in background
        viewModelScope.launchSafe { getConfigurationUseCase(Policy.Network) }

        val initializedState =
            FYAMState(
                configuration,
                taskId?.toUIEvent(),
                url?.toUIEvent(),
                openAppIntegration?.let { IntegrationApp.fromIdentifier(it) }?.toUIEvent()
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

    /* --- device info --- */

    private suspend fun sendDeviceInfo() {

        sendDeviceInfoUseCase()

    }

    /* --- state event --- */

    fun execute(stateEvent: FYAMStateEvent) {

        when (stateEvent) {
            is FYAMStateEvent.Initialize ->
                errorFlow.launchCatch(
                    viewModelScope,
                    FYAMError.Config,
                    loadingFlow,
                    listOf(FYAMLoading.Config, FYAMLoading.Splash)
                )
                {
                    initialize(
                        stateEvent.taskId,
                        stateEvent.url,
                        stateEvent.openAppIntegration,
                        stateEvent.splashLoading
                    )
                }
            FYAMStateEvent.SendDeviceInfo ->
                viewModelScope.launchSafe { sendDeviceInfo() }
        }

    }

}