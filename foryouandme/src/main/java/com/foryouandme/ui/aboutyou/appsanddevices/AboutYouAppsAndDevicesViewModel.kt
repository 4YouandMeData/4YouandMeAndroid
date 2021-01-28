package com.foryouandme.ui.aboutyou.appsanddevices

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.data.datasource.Environment
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.aboutyou.AboutYouDataAppsAndDevicesToAboutYouIntegrationLogin
import com.foryouandme.ui.aboutyou.AboutYouNavController

class AboutYouAppsAndDevicesViewModel(
        navigator: Navigator,
        private val analyticsModule: AnalyticsModule
) :
        BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {

    /*--- data ---*/

    fun createGarminItem(
            environment: Environment,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            isConnected: Boolean
    ): AppAndDeviceItem =
            AppAndDeviceItem(
                    configuration,
                    "Garmin",
                    imageConfiguration.garmin(),
                    isConnected,
                    "${environment.getOAuthBaseUrl()}/users/integration_oauth/garmin",
                    "${environment.getOAuthBaseUrl()}/users/deauthenticate/garmin"
            )

    fun createFitbitItem(
            environment: Environment,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            isConnected: Boolean
    ): AppAndDeviceItem =
            AppAndDeviceItem(
                    configuration,
                    "Fitbit",
                    imageConfiguration.smartwatch(),
                    isConnected,
                    "${environment.getOAuthBaseUrl()}/users/integration_oauth/fitbit",
                    "${environment.getOAuthBaseUrl()}/users/deauthenticate/fitbit"
            )

    fun createOuraItem(
            environment: Environment,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            isConnected: Boolean
    ): AppAndDeviceItem =
            AppAndDeviceItem(
                    configuration,
                    "Oura",
                    imageConfiguration.oura(),
                    isConnected,
                    "${environment.getOAuthBaseUrl()}/users/integration_oauth/oura",
                    "${environment.getOAuthBaseUrl()}/users/deauthenticate/oura"
            )

    fun createInstagramItem(
            environment: Environment,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            isConnected: Boolean
    ): AppAndDeviceItem =
            AppAndDeviceItem(
                    configuration,
                    "Instagram",
                    imageConfiguration.instagram(),
                    isConnected,
                    "${environment.getOAuthBaseUrl()}/users/integration_oauth/instagram",
                    "${environment.getOAuthBaseUrl()}/users/deauthenticate/instagram"
            )

    fun createRescueTimeItem(
            environment: Environment,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            isConnected: Boolean
    ): AppAndDeviceItem =
            AppAndDeviceItem(
                    configuration,
                    "RescueTime",
                    imageConfiguration.rescuetime(),
                    isConnected,
                    "${environment.getOAuthBaseUrl()}/users/integration_oauth/rescuetime",
                    "${environment.getOAuthBaseUrl()}/users/deauthenticate/rescuetime"
            )

    fun createTwitterItem(
            environment: Environment,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            isConnected: Boolean
    ): AppAndDeviceItem =
            AppAndDeviceItem(
                    configuration,
                    "Twitter",
                    imageConfiguration.twitter(),
                    isConnected,
                    "${environment.getOAuthBaseUrl()}/users/integration_oauth/twitter",
                    "${environment.getOAuthBaseUrl()}/users/deauthenticate/twitter"
            )

    /*--- navigation ---*/

    suspend fun navigateToWeb(url: String, navController: AboutYouNavController) {
        navigator.navigateToSuspend(
                navController,
                AboutYouDataAppsAndDevicesToAboutYouIntegrationLogin(
                        url
                )
        )
    }

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
            analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.AppsAndDevices, EAnalyticsProvider.ALL)


}