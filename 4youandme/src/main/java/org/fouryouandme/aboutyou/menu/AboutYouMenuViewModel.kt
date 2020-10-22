package org.fouryouandme.aboutyou.menu

import org.fouryouandme.aboutyou.*
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.navigation.Navigator

class AboutYouMenuViewModel(navigator: Navigator) :
    BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {

    /* --- navigation --- */

    suspend fun toAboutYouReviewConsentPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToAboutYouReviewConsentPage
        )

    suspend fun toAboutYouAppsAndDevicesPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToAppsAndDevicesPage
        )

    suspend fun toAboutYouPermissionsPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToPermissionsPage
        )

    suspend fun toAboutYouUserInfoPage(navController: AboutYouNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToUserInfoPage
        )
}
