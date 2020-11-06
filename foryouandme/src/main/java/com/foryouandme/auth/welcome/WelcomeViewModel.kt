package com.foryouandme.auth.welcome

import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider

class WelcomeViewModel(
    navigator: Navigator,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        Empty,
        Empty,
        Empty,
        Empty>
    (navigator, Empty) {


    suspend fun signUpInfo(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, WelcomeToSignUpInfo)

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.GetStarted, EAnalyticsProvider.ALL)

}