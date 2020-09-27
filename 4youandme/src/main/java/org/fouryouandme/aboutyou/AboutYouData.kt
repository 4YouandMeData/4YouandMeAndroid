package org.fouryouandme.aboutyou

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration


data class AboutYouState(
    val configuration: Configuration
)

sealed class AboutYouStateUpdate {
    data class Initialization(val configuration: Configuration) : AboutYouStateUpdate()
}

sealed class AboutYouLoading {
    object Initialization : AboutYouLoading()
}

sealed class AboutYouError {
    object Initialization : AboutYouError()
}

/* --- navigation --- */

object AboutYouMenuPageToAboutYouReviewConsentPage : NavigationAction

object AboutYouMenuPageToAppsAndDevicesPage : NavigationAction

data class AboutYouDataAppsAndDevicesToIntegrationLogin(
    val url: String
) : NavigationAction
