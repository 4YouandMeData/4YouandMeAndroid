package com.foryouandme.ui.main

import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationAction

data class MainState(
    val restorePage: Int = R.id.feed_navigation
)

sealed class MainStateUpdate {
    data class RestorePage(val selectedPage: Int) : MainStateUpdate()
    data class PageNavigation(val selectedPage: Int) : MainStateUpdate()
}

/* --- navigation --- */

object MainPageToAboutYouPage : NavigationAction

object MainPageToInformation : NavigationAction

object MainPageToReward: NavigationAction

object MainPageToFaq: NavigationAction

data class MainToTask(val id: String) : NavigationAction