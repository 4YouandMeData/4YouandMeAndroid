package org.fouryouandme.aboutyou.permissions

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.entity.configuration.Configuration

class AboutYouPermissionsViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

    fun getPermissions(configuration: Configuration, imageConfiguration: ImageConfiguration) =
        listOf(
            PermissionsItem(
                configuration,
                "1",
                "The BUMP app needs access to your phone's location",
                imageConfiguration.location(),
                true
            ),
            PermissionsItem(
                configuration,
                "2",
                "Push Notifications",
                imageConfiguration.pushNotification(),
                false
            )
        )
}