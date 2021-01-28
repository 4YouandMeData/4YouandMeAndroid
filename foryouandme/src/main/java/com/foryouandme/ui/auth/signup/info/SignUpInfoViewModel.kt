package com.foryouandme.ui.auth.signup.info

import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider

class SignUpInfoViewModel(
    navigator: Navigator,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        Empty,
        Empty,
        Empty,
        Empty>
    (navigator, Empty) {

    /* --- navigation --- */

    suspend fun signUpLater(authNavController: AuthNavController): Unit =
        navigator.navigateToSuspend(authNavController, SignUpInfoToSignUpLater)

    suspend fun enterPhone(authNavController: AuthNavController): Unit =
        navigator.navigateToSuspend(authNavController, SignUpInfoToEnterPhone)

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.RequestSetUp, EAnalyticsProvider.ALL)

}