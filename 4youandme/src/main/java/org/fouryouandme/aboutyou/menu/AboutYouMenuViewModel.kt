package org.fouryouandme.aboutyou.menu

import arrow.fx.ForIO
import org.fouryouandme.aboutyou.*
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class AboutYouMenuViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

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
