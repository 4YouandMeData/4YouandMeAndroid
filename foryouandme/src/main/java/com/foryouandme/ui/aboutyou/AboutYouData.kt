package com.foryouandme.ui.aboutyou

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.user.User

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

object AboutYouMenuToReviewConsent : NavigationAction

object AboutYouMenuToAppsAndDevices : NavigationAction

data class AppsAndDevicesToIntegrationLogin(
    val url: String
) : NavigationAction

object AboutYouMenuToPermissions : NavigationAction

object AboutYouMenuPageToUserInfoPage : NavigationAction

object AboutYouMenuPageToDailySurveyTimePage : NavigationAction