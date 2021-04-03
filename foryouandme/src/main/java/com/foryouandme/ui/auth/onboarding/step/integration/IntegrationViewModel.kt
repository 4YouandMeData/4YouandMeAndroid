package com.foryouandme.ui.auth.onboarding.step.integration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.action.openApp
import com.foryouandme.core.arch.navigation.action.playStoreAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.core.ext.web.asIntegrationCookies
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.integration.GetIntegrationUseCase
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.page.PageRef
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntegrationViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<IntegrationStateUpdate>,
    private val loadingFlow: LoadingFlow<IntegrationLoading>,
    private val errorFlow: ErrorFlow<IntegrationError>,
    private val getIntegrationUseCase: GetIntegrationUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = IntegrationState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- integration --- */

    private suspend fun getIntegration() {

        loadingFlow.show(IntegrationLoading.Integration)

        val integration = getIntegrationUseCase()!!
        val cookies = getTokenUseCase().asIntegrationCookies()

        state = state.copy(integration = integration, cookies = cookies)
        stateUpdateFlow.update(IntegrationStateUpdate.Integration)

        loadingFlow.hide(IntegrationLoading.Integration)

    }







    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.OAuth,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: IntegrationStateEvent) {
        when (stateEvent) {
            IntegrationStateEvent.GetIntegration ->
                errorFlow.launchCatch(
                    viewModelScope,
                    IntegrationError.Integration,
                    loadingFlow,
                    IntegrationLoading.Integration
                ) { getIntegration() }
            IntegrationStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }
    }

}