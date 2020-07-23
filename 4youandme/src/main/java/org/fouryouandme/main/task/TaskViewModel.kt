package org.fouryouandme.main.task

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

class TaskViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        TaskState,
        TaskStateUpdate,
        TaskError,
        TaskLoading>
    (TaskState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(TaskLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)
                    .handleAuthError(runtime, rootNavController, navigator)

            !configuration.fold(
                { setError(it, TaskError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        TaskStateUpdate.Initialization(it)
                    )
                }
            )

            !hideLoading(TaskLoading.Initialization)

        }.unsafeRunAsync()

}