package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.aboutyou.menu.AboutYouMenuFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun aboutYouMenuPageToAboutYouReviewConsentPage(): NavigationExecution =
    {
        it.navigate(AboutYouMenuFragmentDirections.actionAboutYouMenuToAboutYouReviewConsent())
    }