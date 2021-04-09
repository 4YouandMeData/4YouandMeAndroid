package com.foryouandme.ui.aboutyou.menu

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.ui.aboutyou.*

class AboutYouMenuViewModel(navigator: Navigator, private val analyticsModule: AnalyticsModule) :
    BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {

    /* --- navigation --- */

    suspend fun toAboutYouReviewConsentPage(navController: AboutYouNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            AboutYouMenuPageToAboutYouReviewConsentPage
        )

    suspend fun toAboutYouAppsAndDevicesPage(navController: AboutYouNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            AboutYouMenuPageToAppsAndDevicesPage
        )

    suspend fun toAboutYouPermissionsPage(navController: AboutYouNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            AboutYouMenuPageToPermissionsPage
        )

    suspend fun toAboutYouUserInfoPage(navController: AboutYouNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            AboutYouMenuPageToUserInfoPage
        )

    suspend fun toAboutYouDailySurveyTimePage(navController: AboutYouNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            AboutYouMenuPageToDailySurveyTimePage
        )

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.AboutYou, EAnalyticsProvider.ALL)

}
