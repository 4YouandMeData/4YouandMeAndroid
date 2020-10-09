package org.fouryouandme.aboutyou

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.user.User

data class AboutYouState(val user: User)

sealed class AboutYouStateUpdate {

    data class Initialization(val user: User) : AboutYouStateUpdate()
    data class Refresh(val user: User) : AboutYouStateUpdate()

}

sealed class AboutYouLoading {

    object Initialization : AboutYouLoading()
    object Refresh : AboutYouLoading()

}

sealed class AboutYouError {

    object Initialization : AboutYouError()
    object Refresh : AboutYouError()
}

/* --- navigation --- */

object AboutYouMenuPageToAboutYouReviewConsentPage : NavigationAction

object AboutYouMenuPageToAppsAndDevicesPage : NavigationAction

data class AboutYouDataAppsAndDevicesToIntegrationLogin(
    val url: String
) : NavigationAction

object AboutYouMenuPageToPermissionsPage : NavigationAction

object AboutYouMenuPageToUserInfoPage : NavigationAction
