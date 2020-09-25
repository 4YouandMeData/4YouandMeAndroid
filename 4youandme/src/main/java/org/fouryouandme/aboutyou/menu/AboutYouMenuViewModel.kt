package org.fouryouandme.aboutyou.menu

import arrow.fx.ForIO
import org.fouryouandme.aboutyou.AboutYouMenuPageToAboutYouReviewConsentPage
import org.fouryouandme.aboutyou.AboutYouSectionNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class AboutYouMenuViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

    /* --- navigation --- */

    suspend fun toAboutYouReviewConsentPage(navController: AboutYouSectionNavController): Unit =
        navigator.navigateTo(
            navController,
            AboutYouMenuPageToAboutYouReviewConsentPage
        )
}
