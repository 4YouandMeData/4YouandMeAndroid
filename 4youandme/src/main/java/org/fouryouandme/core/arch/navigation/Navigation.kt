package org.fouryouandme.core.arch.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import arrow.Kind
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.ext.evalOnMain

typealias NavigationExecution = (NavController) -> Unit

class Navigator(
    private val navigationProvider: NavigationProvider
) {

    private val activityActionLiveData = MutableLiveData<Event<ActivityAction>>()

    fun <F> navigateTo(
        runtime: Runtime<F>,
        controller: ParentNavController,
        navigationAction: NavigationAction
    ): Kind<F, Unit> =
        navigateTo(runtime, controller.navController, navigationAction)

    fun <F> navigateTo(
        runtime: Runtime<F>,
        controller: NavController,
        navigationAction: NavigationAction
    ): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher {

                val navigation =
                    navigationProvider.getNavigation(navigationAction)

                navigation(controller)

            }
        }

    fun <F> back(runtime: Runtime<F>, navController: ParentNavController): Kind<F, Boolean> =
        back(runtime, navController.navController)

    fun <F> back(runtime: Runtime<F>, navController: NavController): Kind<F, Boolean> =
        runtime.fx.concurrent { navController.popBackStack() }

    @Deprecated("use the suspend version")
    fun <F> performAction(runtime: Runtime<F>, action: ActivityAction): Kind<F, Unit> =
        runtime.fx.concurrent {

            !runtime.onMainDispatcher { activityActionLiveData.value = action.toEvent() }

        }

    suspend fun performAction(action: ActivityAction): Unit =
        evalOnMain { activityActionLiveData.value = action.toEvent() }

    fun activityAction(): LiveData<Event<ActivityAction>> = activityActionLiveData
}

interface NavigationProvider {
    fun getNavigation(action: NavigationAction): NavigationExecution
}

abstract class ParentNavController(val navController: NavController)