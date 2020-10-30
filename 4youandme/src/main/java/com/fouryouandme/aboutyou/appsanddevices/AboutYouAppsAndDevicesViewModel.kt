package com.fouryouandme.aboutyou.appsanddevices

import com.fouryouandme.aboutyou.AboutYouDataAppsAndDevicesToAboutYouIntegrationLogin
import com.fouryouandme.aboutyou.AboutYouNavController
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.deps.ImageConfiguration
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.entity.configuration.Configuration

class AboutYouAppsAndDevicesViewModel(navigator: Navigator) :
    BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {

    /*--- data ---*/

    fun createGarminItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "Garmin",
            imageConfiguration.garmin(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/garmin"
        )

    fun createFitbitItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "Fitbit",
            imageConfiguration.smartwatch(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/fitbit"
        )

    fun createOuraItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "Oura",
            imageConfiguration.oura(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/oura"
        )

    fun createInstagramItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "Instagram",
            imageConfiguration.instagram(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/instagram"
        )

    fun createRescueTimeItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "RescueTime",
            imageConfiguration.rescuetime(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/rescuetime"
        )

    fun createTwitterItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "Twitter",
            imageConfiguration.twitter(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/twitter"
        )

/*--- navigation ---*/

    suspend fun navigateToWeb(url: String, navController: AboutYouNavController) {
        navigator.navigateTo(
            navController,
            AboutYouDataAppsAndDevicesToAboutYouIntegrationLogin(
                url
            )
        )
    }
}