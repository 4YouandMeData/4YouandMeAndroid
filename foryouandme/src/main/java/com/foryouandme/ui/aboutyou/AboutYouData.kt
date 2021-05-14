package com.foryouandme.ui.aboutyou

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User

data class AboutYouState(
    val user: LazyData<User> = LazyData.Empty,
    val configuration: LazyData<Configuration> = LazyData.Empty
)

sealed class AboutYouAction {
    object GetUser: AboutYouAction()
    object GetConfiguration: AboutYouAction()
    object ScreenViewed: AboutYouAction()
}

/* --- navigation --- */

object AboutYouToReviewConsent : NavigationAction

object AboutYouToAppsAndDevices : NavigationAction

object AboutYouToPermissions : NavigationAction

object AboutYouToUserInfo : NavigationAction

object AboutYouToDailySurveyTime : NavigationAction