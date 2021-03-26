package com.foryouandme.core.arch.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.foryouandme.R
import com.foryouandme.core.arch.livedata.Event
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.arch.navigation.action.ActivityAction
import com.foryouandme.core.ext.evalOnMain
import javax.inject.Inject
import javax.inject.Singleton

typealias NavigationExecution = (NavController) -> Unit

@Singleton
class Navigator @Inject constructor(
    private val navigationProvider: ForYouAndMeNavigationProvider
) {

    private val activityActionLiveData = MutableLiveData<Event<ActivityAction>>()

    suspend fun navigateToSuspend(
        controller: FYAMNavController,
        navigationAction: NavigationAction
    ): Unit =
        evalOnMain {

            val navigation =
                navigationProvider.getNavigation(navigationAction)

            navigation(controller.navController)

        }

    fun navigateTo(
        controller: FYAMNavController,
        navigationAction: NavigationAction
    ) {

        val navigation = navigationProvider.getNavigation(navigationAction)
        navigation(controller.navController)

    }

    suspend fun backSuspend(navController: FYAMNavController): Boolean =
        backSuspend(navController.navController)

    fun back(navController: FYAMNavController): Boolean =
        navController.navController.popBackStack()

    suspend fun close(rootNavController: RootNavController): Boolean =
        rootNavController.navController.popBackStack(R.id.navigation, true)

    suspend fun backSuspend(navController: NavController): Boolean =
        navController.popBackStack()

    suspend fun performActionSuspend(action: ActivityAction): Unit =
        evalOnMain { activityActionLiveData.value = action.toEvent() }

    fun performAction(action: ActivityAction) {
        activityActionLiveData.value = action.toEvent()
    }


    fun activityAction(): LiveData<Event<ActivityAction>> = activityActionLiveData
}

abstract class FYAMNavController(val navController: NavController)