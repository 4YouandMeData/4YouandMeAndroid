package com.foryouandme.ui.aboutyou.menu

import com.foryouandme.ui.aboutyou.*
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider

class AboutYouMenuViewModel(navigator: Navigator, private val analyticsModule: AnalyticsModule) :
    BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {

    /* --- navigation --- */

    suspend fun toAboutYouReviewConsentPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToAboutYouReviewConsentPage
        )

    suspend fun toAboutYouAppsAndDevicesPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToAppsAndDevicesPage
        )

    suspend fun toAboutYouPermissionsPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToPermissionsPage
        )

    suspend fun toAboutYouUserInfoPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToUserInfoPage
        )

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.AboutYou, EAnalyticsProvider.ALL)

}
