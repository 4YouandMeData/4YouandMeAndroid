package com.foryouandme.ui.auth.signup.later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpLaterViewModel @Inject constructor(
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.SetupLater,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: SignUpLaterStateEvent) {
        when (stateEvent) {
            SignUpLaterStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }

    }

}