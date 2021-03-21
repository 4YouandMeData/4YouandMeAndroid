package com.foryouandme.ui.auth.signup.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpInfoViewModel @Inject constructor(
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.RequestSetUp,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: SignUpInfoStateEvent) {
        when (stateEvent) {
            SignUpInfoStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }

    }

}