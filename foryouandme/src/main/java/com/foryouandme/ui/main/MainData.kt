package com.foryouandme.ui.main

import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.studyinfo.detail.EStudyInfoType

data class MainState(
    val configuration: LazyData<Configuration> = LazyData.Empty
)

sealed class MainAction {

    object GetConfiguration : MainAction()
    data class LogScreenSelected(val screen: Screen.HomeScreen) : MainAction()
    data class HandleDeepLink(val fyamState: FYAMState) : MainAction()

}

sealed class MainEvent {

    data class OpenUrl(val url: String) : MainEvent()
    data class OpenTask(val taskId: String) : MainEvent()
    data class OpenApp(val packageName: String) : MainEvent()

}

/* --- navigation --- */

object MainToAboutYou : NavigationAction

data class MainToStudyInfoDetail(val type: EStudyInfoType) : NavigationAction

data class MainToTask(val id: String) : NavigationAction