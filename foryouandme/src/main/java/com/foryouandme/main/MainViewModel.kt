package com.foryouandme.main

import com.foryouandme.R
import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.openApp

class MainViewModel(
    navigator: Navigator,
) : BaseViewModel<
        MainState,
        MainStateUpdate,
        Empty,
        Empty>
    (navigator, MainState()) {

    /* --- page --- */

    suspend fun setRestorePage(id: Int): Unit {

        setState(state().copy(restorePage = id)) { MainStateUpdate.RestorePage(id) }

    }

    suspend fun selectFeed(): Unit =
        setState(
            state()
        ) { MainStateUpdate.PageNavigation(R.id.feed_navigation) }

    fun getPagedIds(): List<Int> =
        listOf(
            R.navigation.feed_navigation,
            R.navigation.tasks_navigation,
            R.navigation.user_data_navigation,
            R.navigation.study_info_navigation
        )


    /* --- deep link --- */

    suspend fun handleDeepLink(rootNavController: RootNavController, fyamState: FYAMState): Unit {

        val taskId =
            fyamState.taskId?.getContentOnce()?.orNull()?.t
        val url =
            fyamState.url?.getContentOnce()?.orNull()?.t
        val openApplicationIntegration =
            fyamState.openAppIntegration?.getContentOnce()?.orNull()?.t

        when {

            taskId != null ->
                navigator.navigateTo(rootNavController, MainToTask(taskId))
            url != null ->
                navigator.navigateTo(rootNavController, AnywhereToWeb(url))
            openApplicationIntegration != null ->
                navigator.performAction(openApp(openApplicationIntegration.packageName))

        }

    }

}