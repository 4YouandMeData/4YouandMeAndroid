package com.foryouandme.ui.auth.signin.pin

import androidx.lifecycle.ViewModel
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.toastAction
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.ui.auth.AuthNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinCodeViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<PinCodeStateUpdate>,
    private val loadingFlow: LoadingFlow<PinCodeLoading>,
    private val errorFlow: ErrorFlow<PinCodeError>,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = PinCodeState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- analytics --- */

    private suspend fun logScreenViewed(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.UserRegistration,
            EAnalyticsProvider.ALL
        )

    private suspend fun logPrivacyPolicy(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.PrivacyPolicy,
            EAnalyticsProvider.ALL
        )

    private suspend fun logTermsOfService(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.TermsOfService,
            EAnalyticsProvider.ALL
        )

}