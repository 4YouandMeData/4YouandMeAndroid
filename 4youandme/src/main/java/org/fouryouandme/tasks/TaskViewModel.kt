package org.fouryouandme.tasks

import arrow.core.Option
import arrow.core.some
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.tasks.step.Step

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

    fun initialize(): Unit =
        runtime.fx.concurrent {

            !showLoading(TaskLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)


            !configuration.fold(
                { setError(it, TaskError.Initialization) },
                {

                    // TODO: handle dynamic task creation
                    val task = Task.ReactionTimeTask(it)


                    setState(
                        state().copy(
                            configuration = it.toOption(),
                            task = task.some()
                        ),
                        TaskStateUpdate.Initialization(it, task)
                    )
                }
            )

            !hideLoading(TaskLoading.Initialization)

        }.unsafeRunAsync()


    /* --- step --- */

    fun getStepByIndex(index: Int): Option<Step> =
        state().task.flatMap { it.steps.getOrNull(index).toOption() }

}