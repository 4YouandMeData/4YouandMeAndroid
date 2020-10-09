package org.fouryouandme.aboutyou

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration


/* --- navigation --- */

object AboutYouMenuPageToAboutYouReviewConsentPage : NavigationAction

object AboutYouMenuPageToAppsAndDevicesPage : NavigationAction

data class AboutYouDataAppsAndDevicesToIntegrationLogin(
    val url: String
) : NavigationAction

object AboutYouMenuPageToPermissionsPage : NavigationAction

object AboutYouMenuPageToUserInfoPage : NavigationAction
