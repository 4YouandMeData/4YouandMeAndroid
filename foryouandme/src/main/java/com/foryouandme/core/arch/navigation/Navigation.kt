package com.foryouandme.core.arch.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.foryouandme.R
import com.foryouandme.core.arch.livedata.Event
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.ext.evalOnMain

typealias NavigationExecution = (NavController) -> Unit

class Navigator(
    private val navigationProvider: NavigationProvider
) {

    private val activityActionLiveData = MutableLiveData<Event<ActivityAction>>()

    suspend fun navigateTo(
        controller: FYAMNavController,
        navigationAction: NavigationAction
    ): Unit =
        evalOnMain {

            val navigation =
                navigationProvider.getNavigation(navigationAction)

            navigation(controller.navController)

        }

    suspend fun back(navController: FYAMNavController): Boolean =
        back(navController.navController)

    suspend fun close(rootNavController: RootNavController): Boolean =
        rootNavController.navController.popBackStack(R.id.navigation, true)

    suspend fun back(navController: NavController): Boolean =
        navController.popBackStack()

    suspend fun performAction(action: ActivityAction): Unit =
        evalOnMain { activityActionLiveData.value = action.toEvent() }

    fun activityAction(): LiveData<Event<ActivityAction>> = activityActionLiveData
}

interface NavigationProvider {
    fun getNavigation(action: NavigationAction): NavigationExecution
}

abstract class FYAMNavController(val navController: NavController)