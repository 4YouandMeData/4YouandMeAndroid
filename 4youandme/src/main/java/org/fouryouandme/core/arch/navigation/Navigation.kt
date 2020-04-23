package org.fouryouandme.core.arch.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import arrow.Kind
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent

typealias NavigationExecution = (NavController) -> Unit

abstract class Navigator(
    private val navigationProvider: NavigationProvider
) {

    private val activityActionLiveData = MutableLiveData<Event<ActivityAction>>()

    fun <F> navigateTo(
        runtime: Runtime<F>,
        controller: NavController,
        navigationAction: NavigationAction
    ): Kind<F, Unit> =
        runtime.fx.concurrent {

            continueOn(runtime.context.mainDispatcher)

            val navigation =
                navigationProvider.getNavigation(navigationAction)

            navigation(controller)

            continueOn(runtime.context.bgDispatcher)
        }

    fun <F> performAction(runtime: Runtime<F>, action: ActivityAction): Kind<F, Unit> =
        runtime.fx.concurrent {

            continueOn(runtime.context.mainDispatcher)

            activityActionLiveData.value = action.toEvent()

            continueOn(runtime.context.bgDispatcher)
        }

    fun activityAction(): LiveData<Event<ActivityAction>> = activityActionLiveData
}

interface NavigationProvider {
    fun getNavigation(action: NavigationAction): NavigationExecution
}