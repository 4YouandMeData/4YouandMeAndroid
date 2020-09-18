package org.fouryouandme.main

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration

data class MainState(
    val configuration: Option<Configuration> = None,
    val restorePage: Int = R.id.feed_navigation
)

sealed class MainStateUpdate {
    data class Initialization(val configuration: Configuration) : MainStateUpdate()
    data class RestorePage(val selectedPage: Int) : MainStateUpdate()
    data class PageNavigation(val selectedPage: Int) : MainStateUpdate()
}

sealed class MainLoading {
    object Initialization : MainLoading()
}

sealed class MainError {
    object Initialization : MainError()
}

/* --- navigation --- */

object MainPageToAboutYouPage : NavigationAction