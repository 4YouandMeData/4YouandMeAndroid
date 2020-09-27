package org.fouryouandme.aboutyou.permissions

import arrow.fx.ForIO
import org.fouryouandme.aboutyou.AboutYouDataAppsAndDevicesToIntegrationLogin
import org.fouryouandme.aboutyou.AboutYouNavController
import org.fouryouandme.aboutyou.appsanddevices.AppAndDeviceItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.entity.configuration.Configuration

class AboutYouPermissionsViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

    fun getPermissions(configuration: Configuration, imageConfiguration: ImageConfiguration) =
        listOf(
            Empty
//            AppAndDeviceItem(
//                configuration,
//                "1",
//                "Garmin",
//                imageConfiguration.fitbit(),
//                true,
//                "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/garmin"
//            ),
//            AppAndDeviceItem(
//                configuration,
//                "2",
//                "Oura",
//                imageConfiguration.oura(),
//                true,
//                "https://admin-4youandme-staging.balzo.eu/users/integration_oauth/oura"
//            )
        )
}