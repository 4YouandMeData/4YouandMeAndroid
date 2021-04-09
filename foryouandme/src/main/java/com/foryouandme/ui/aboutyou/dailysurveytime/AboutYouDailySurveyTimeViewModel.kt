package com.foryouandme.ui.aboutyou.dailysurveytime

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.DailySurveyTimeModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.entity.configuration.Configuration

class AboutYouDailySurveyTimeViewModel(
    navigator: Navigator,
    private val dailySurveyTimeModule: DailySurveyTimeModule,
    private val analyticsModule: AnalyticsModule
) :
    BaseViewModel<
            AboutYouDailySurveyTimeState,
            AboutYouDailySurveyTimeStateUpdate,
            Empty,
            AboutYouDailySurveyTimeLoading>(navigator) {

    suspend fun initialize(configuration: Configuration) {

        showLoading(AboutYouDailySurveyTimeLoading.Initialization)

        // TODO

        //setState(AboutYouDailySurveyTimeState())
        //{ AboutYouDailySurveyTimeStateUpdate.Initialization(it.) }

        hideLoading(AboutYouDailySurveyTimeLoading.Initialization)

    }

    /* --- navigation --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.DailySurveyTime, EAnalyticsProvider.ALL)

}