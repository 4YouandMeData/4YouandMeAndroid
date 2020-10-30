package com.fouryouandme.core.arch.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.fouryouandme.core.arch.livedata.Event
import com.fouryouandme.core.arch.livedata.toEvent
import com.fouryouandme.core.ext.evalOnMain

typealias NavigationExecution = (NavController) -> Unit

class Navigator(
    private val navigationProvider: NavigationProvider
) {

    private val activityActionLiveData = MutableLiveData<Event<ActivityAction>>()

    suspend fun navigateTo(
        controller: FYAMNavController,
        navigationAction: NavigationAction
    ): Unit =
        navigateTo(controller.navController, navigationAction)

    suspend fun navigateTo(
        controller: NavController,
        navigationAction: NavigationAction
    ): Unit {

        evalOnMain {

            val navigation =
                navigationProvider.getNavigation(navigationAction)

            navigation(controller)

        }
    }

    suspend fun back(navController: FYAMNavController): Boolean =
        back(navController.navController)

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