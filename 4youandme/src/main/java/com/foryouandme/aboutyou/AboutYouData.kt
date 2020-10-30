package com.foryouandme.aboutyou

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.core.entity.user.User

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

data class AboutYouDataAppsAndDevicesToAboutYouIntegrationLogin(
    val url: String
) : NavigationAction

object AboutYouMenuPageToPermissionsPage : NavigationAction

object AboutYouMenuPageToUserInfoPage : NavigationAction
