package org.fouryouandme.aboutyou.appsanddevices

import arrow.fx.ForIO
import org.fouryouandme.aboutyou.AboutYouDataAppsAndDevicesToIntegrationLogin
import org.fouryouandme.aboutyou.AboutYouNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.entity.configuration.Configuration

class AboutYouAppsAndDevicesViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

    fun getAppAndDevices(configuration: Configuration, imageConfiguration: ImageConfiguration) =
        listOf(
            AppAndDeviceItem(
                configuration,
                "1",
                "Garmin",
                imageConfiguration.fitbit(),
                true,
                "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/garmin"
            ),
            AppAndDeviceItem(
                configuration,
                "2",
                "Oura",
                imageConfiguration.oura(),
                false,
                "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/oura"
            )
        )

    suspend fun navigateToWeb(url: String, navController: AboutYouNavController) {
        navigator.navigateTo(navController, AboutYouDataAppsAndDevicesToIntegrationLogin(url))
    }
}