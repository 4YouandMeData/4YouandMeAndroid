package org.fouryouandme.main

import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationAction

data class MainState(
    val restorePage: Int = R.id.feed_navigation
)

sealed class MainStateUpdate {
    data class RestorePage(val selectedPage: Int) : MainStateUpdate()
    data class PageNavigation(val selectedPage: Int) : MainStateUpdate()
}

/* --- navigation --- */

object MainPageToAboutYouPage : NavigationAction
data class MainPageToHtmlDetailsPage(val pageId: Int) : NavigationAction