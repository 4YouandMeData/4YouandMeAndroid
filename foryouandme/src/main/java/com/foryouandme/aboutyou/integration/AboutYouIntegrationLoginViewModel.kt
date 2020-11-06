package com.foryouandme.aboutyou.integration

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider

class AboutYouIntegrationLoginViewModel(
    navigator: Navigator,
    private val analyticsModule: AnalyticsModule
) :
    BaseViewModel<
            Empty,
            Empty,
            Empty,
            Empty>
        (navigator, Empty) {

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.OAuth, EAnalyticsProvider.ALL)

}