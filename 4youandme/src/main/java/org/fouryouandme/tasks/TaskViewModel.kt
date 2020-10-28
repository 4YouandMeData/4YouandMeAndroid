package org.fouryouandme.tasks

import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.foldSuspend
import org.fouryouandme.researchkit.result.StepResult
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.result.results
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepNavController
import org.fouryouandme.researchkit.task.TaskConfiguration
import org.fouryouandme.researchkit.task.TaskHandleResult
import timber.log.Timber

class TaskViewModel(
    navigator: Navigator,
    private val taskConfiguration: TaskConfiguration,
) : BaseViewModel<
        TaskState,
        TaskStateUpdate,
        TaskError,
        TaskLoading>
    (navigator = navigator) {

    /* --- initialization --- */

    suspend fun initialize(type: String, id: String, data: Map<String, String>): Unit {

        showLoading(TaskLoading.Initialization)

        taskConfiguration.build(type, id, data)
            .foldSuspend(
                { setError(unknownError(), TaskError.Initialization) },
                { task ->

                    setState(
                        TaskState(
                            task = task,
                            isCancelled = false,
                            isCompleted = false,
                            result = TaskResult(task.type, emptyMap())
                        )
                    ) { TaskStateUpdate.Initialization(it.task) }
                }
            )

        hideLoading(TaskLoading.Initialization)

    }

    /* --- state --- */

    suspend fun cancel(): Unit {
        setState(state().copy(isCancelled = true))
        { TaskStateUpdate.Cancelled(it.isCancelled) }
    }

    suspend fun end(): Unit {

        showLoading(TaskLoading.Result)

        val result =
            taskConfiguration.handleTaskResult(state().result, state().task.type, state().task.id)

        evalOnMain { taskConfiguration.taskResultLiveData.value = result.toEvent() }

        when (result) {
            TaskHandleResult.Handled ->
                setState(TaskState.isCompleted.modify(state()) { true })
                { TaskStateUpdate.Completed }
            is TaskHandleResult.Error ->
                setError(unknownError(), TaskError.Result)
        }

        hideLoading(TaskLoading.Result)

    }

    suspend fun addResult(result: StepResult): Unit {

        setStateSilent(
            TaskState.result.results.modify(state()) {
                val map = it.toMutableMap()
                map[result.identifier] = result
                map
            }
        )

    }

    /* --- step --- */

    fun getStepByIndex(index: Int): Step? = state().task.steps.getOrNull(index)

    fun getStepById(id: String): Step? = state().task.steps.firstOrNull { it.identifier == id }

    inline fun <reified T : Step> getStepByIndexAs(index: Int): T? = getStepByIndex(index) as? T

    fun canGoBack(stepIndex: Int): Boolean =
        getStepByIndex(stepIndex - 1)?.back != null

    /* --- navigation --- */

    suspend fun nextStep(stepNavController: StepNavController, currentStepIndex: Int): Unit =
        getStepByIndex(currentStepIndex + 1)
            .foldSuspend(
                { end() },
                {
                    navigator.navigateTo(
                        stepNavController,
                        StepToStep(currentStepIndex + 1)
                    )
                }
            )

    suspend fun skipToStep(
        stepNavController: StepNavController,
        stepId: String,
        currentStepIndex: Int
    ): Unit =
        if (stepId.contains("exit")) end()
        else getStepById(stepId)
            .foldSuspend(
                {
                    Timber.tag(TAG)
                        .e("Unable to skip to step $stepId for task ${state().task.id}, there is no step with this id")
                    nextStep(stepNavController, currentStepIndex)
                },
                {
                    val skipIndex = state().task.steps.indexOf(it)

                    if (skipIndex <= currentStepIndex) {
                        Timber.tag(TAG)
                            .e("Unable to skip to step $stepId for task ${state().task.id}, the step index is <= to the index of the current step")
                        nextStep(stepNavController, currentStepIndex)
                    } else
                        navigator.navigateTo(stepNavController, StepToStep(skipIndex))
                }
            )

    suspend fun close(taskNavController: TaskNavController): Unit {
        navigator.back(taskNavController)
    }

    suspend fun back(
        stepIndex: Int,
        stepNavController: StepNavController,
        taskNavController: TaskNavController
    ): Unit {

        if (canGoBack(stepIndex) && navigator.back(stepNavController).not())
            navigator.back(taskNavController)

    }

    companion object {

        private const val TAG: String = "Research Kit"
    }
}