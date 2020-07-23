package org.fouryouandme.main.tasks

import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class TasksViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        TasksState,
        TasksStateUpdate,
        TasksError,
        TasksLoading>
    (TasksState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(TasksLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)
                    .handleAuthError(runtime, rootNavController, navigator)

            !configuration.fold(
                { setError(it, TasksError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        TasksStateUpdate.Initialization(it)
                    )
                }
            )

            !hideLoading(TasksLoading.Initialization)

        }.unsafeRunAsync()

}