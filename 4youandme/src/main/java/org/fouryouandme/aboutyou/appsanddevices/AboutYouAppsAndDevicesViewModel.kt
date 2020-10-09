package org.fouryouandme.aboutyou.appsanddevices

import arrow.fx.ForIO
import org.fouryouandme.aboutyou.AboutYouDataAppsAndDevicesToIntegrationLogin
import org.fouryouandme.aboutyou.AboutYouNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.entity.configuration.Configuration

class AboutYouAppsAndDevicesViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

    /*--- data ---*/

    fun createGarminItem(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        isConnected: Boolean
    ): AppAndDeviceItem =
        AppAndDeviceItem(
            configuration,
            "Garmin",
            imageConfiguration.fitbit(),
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
            imageConfiguration.fitbit(),
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
            imageConfiguration.oura(),
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
        imageConfiguration.oura(),
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
            imageConfiguration.oura(),
            isConnected,
            "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/twitter"
        )

/*--- navigation ---*/

    suspend fun navigateToWeb(url: String, navController: AboutYouNavController) {
        navigator.navigateTo(navController, AboutYouDataAppsAndDevicesToIntegrationLogin(url))
    }
}