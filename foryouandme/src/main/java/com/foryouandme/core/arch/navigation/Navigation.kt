package com.foryouandme.core.arch.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.foryouandme.R
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.arch.navigation.action.ActivityAction
import com.foryouandme.core.ext.catchToNull
import javax.inject.Inject
import javax.inject.Singleton

typealias NavigationExecution = (NavController) -> Unit

@Singleton
class Navigator @Inject constructor(
    private val navigationProvider: ForYouAndMeNavigationProvider
) {

    private val activityActionLiveData = MutableLiveData<UIEvent<ActivityAction>>()

    fun navigateTo(
        controller: FYAMNavController,
        navigationAction: NavigationAction
    ) {

        val navigation = navigationProvider.getNavigation(navigationAction)
        catchToNull { navigation(controller.navController) }
    }

    fun back(navController: FYAMNavController): Boolean =
        navController.navController.popBackStack()

    fun close(rootNavController: RootNavController): Boolean =
        rootNavController.navController.popBackStack(R.id.navigation, true)

    fun performAction(action: ActivityAction) {
        activityActionLiveData.value = action.toUIEvent()
    }

    fun activityAction(): LiveData<UIEvent<ActivityAction>> = activityActionLiveData
}

abstract class FYAMNavController(val navController: NavController)