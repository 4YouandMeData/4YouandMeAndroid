package com.foryouandme.ui.main

import com.foryouandme.R
import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class MainState(
    val configuration: Configuration? = null,
    val restorePage: Int = R.id.feed_navigation
)

sealed class MainStateUpdate {
    data class Config(val configuration: Configuration) : MainStateUpdate()
    data class RestorePage(val selectedPage: Int) : MainStateUpdate()
    data class PageNavigation(val selectedPage: Int) : MainStateUpdate()
}

sealed class MainLoading {

    object Config : MainLoading()

}

sealed class MainError {

    object Config : MainError()

}

sealed class MainStateEvent {

    object GetConfig : MainStateEvent()
    data class LoadPageSelected(val itemId: Int) : MainStateEvent()
    data class SetRestorePage(val itemId: Int) : MainStateEvent()
    data class HandleDeepLink(val fyamState: FYAMState) : MainStateEvent()
    object SelectFeed : MainStateEvent()
    object SelectTasks : MainStateEvent()
    object SelectYourData : MainStateEvent()
    object SelectStudyInfo : MainStateEvent()

}

/* --- navigation --- */

object MainPageToAboutYouPage : NavigationAction

object MainPageToInformation : NavigationAction

object MainPageToReward : NavigationAction

object MainPageToFaq : NavigationAction

data class MainToTask(val id: String) : NavigationAction