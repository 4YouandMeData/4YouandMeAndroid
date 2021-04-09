package com.foryouandme.ui.aboutyou.dailysurveytime

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.UserSettingsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.usersettings.UserSettingsUseCase.getUserSettings
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.entity.configuration.Configuration

class AboutYouDailySurveyTimeViewModel(
    navigator: Navigator,
    private val userSettingsModule: UserSettingsModule,
    private val analyticsModule: AnalyticsModule
) :
    BaseViewModel<
            AboutYouDailySurveyTimeState,
            AboutYouDailySurveyTimeStateUpdate,
            AboutYouDailySurveyTimeError,
            AboutYouDailySurveyTimeLoading>(navigator) {

    suspend fun initialize(configuration: Configuration) {

        showLoading(AboutYouDailySurveyTimeLoading.Initialization)

        val userSettingsResponse =
            suspend {
                userSettingsModule.getUserSettings()
            }

        //setState(AboutYouDailySurveyTimeState())
        //{ AboutYouDailySurveyTimeStateUpdate.Initialization(it.) }

        hideLoading(AboutYouDailySurveyTimeLoading.Initialization)

    }

    suspend fun sendTimeUpdate(): Unit {

        showLoading(AboutYouDailySurveyTimeLoading.Upload)



        hideLoading(AboutYouDailySurveyTimeLoading.Upload)

    }

    /* --- navigation --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.DailySurveyTime, EAnalyticsProvider.ALL)

}