package org.fouryouandme.tasks

import androidx.navigation.NavController
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.ext.foldSuspend
import org.fouryouandme.researchkit.result.StepResult
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.result.results
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepNavController
import org.fouryouandme.researchkit.task.TaskBuilder

class TaskViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val taskBuilder: TaskBuilder,
) : BaseViewModel<
        ForIO,
        TaskState,
        TaskStateUpdate,
        TaskError,
        TaskLoading>
    (navigator = navigator, runtime = runtime) {

    /* --- initialization --- */

    suspend fun initialize(identifier: String): Unit {

        showLoadingFx(TaskLoading.Initialization)

        taskBuilder.buildByIdentifier(identifier)
            .foldSuspend(
                { setErrorFx(unknownError(), TaskError.Initialization) },
                { task ->

                    setStateFx(
                        TaskState(
                            task = task,
                            isCancelled = false,
                            result = TaskResult(task.identifier, emptyMap())
                        )
                    ) { TaskStateUpdate.Initialization(it.task) }
                }
            )

        hideLoadingFx(TaskLoading.Initialization)

    }

    /* --- state --- */

    suspend fun cancel(): Unit =
        setStateFx(
            state().copy(isCancelled = true)
        ) { TaskStateUpdate.Cancelled(it.isCancelled) }

    suspend fun addResult(result: StepResult): Unit {

        setStateSilentFx(
            TaskState.result.results.modify(state()) {
                val map = it.toMutableMap()
                map[result.identifier] = result
                map
            }
        )

    }

    /* --- step --- */

    fun getStepByIndex(index: Int): Step? = state().task.steps.getOrNull(index)

    inline fun <reified T : Step> getStepByIndexAs(index: Int): T? = getStepByIndex(index) as? T

    /* --- navigation --- */

    suspend fun nextStep(navController: NavController, currentStepIndex: Int): Unit =
        getStepByIndex(currentStepIndex + 1)
            .foldSuspend(
                { Unit /* TODO: Handle task completed */ },
                {
                    navigator.navigateTo(
                        navController,
                        StepToStep(currentStepIndex + 1)
                    )
                }
            )

    suspend fun close(taskNavController: TaskNavController): Unit {
        navigator.back(taskNavController)
    }

    // TODO: check previous step
    suspend fun back(
        stepNavController: StepNavController,
        taskNavController: TaskNavController
    ): Unit {

        if (navigator.back(stepNavController).not())
            navigator.back(taskNavController)

    }
}