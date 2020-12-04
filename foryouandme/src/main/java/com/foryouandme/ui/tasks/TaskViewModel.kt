package com.foryouandme.ui.tasks

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.error.unknownError
import com.foryouandme.core.arch.livedata.toEvent
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.foldSuspend
import com.foryouandme.researchkit.result.StepResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.result.results
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.StepNavController
import com.foryouandme.researchkit.task.TaskConfiguration
import com.foryouandme.researchkit.task.TaskResponse
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

    suspend fun initialize(id: String, data: Map<String, String>): Unit {

        showLoading(TaskLoading.Initialization)

        taskConfiguration.build(id, data)
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
            TaskResponse.Success ->
                setState(TaskState.isCompleted.modify(state()) { true })
                { TaskStateUpdate.Completed }
            is TaskResponse.Error ->
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
                    taskConfiguration.onStepLoaded(state().task, it)
                    navigator.navigateTo(
                        stepNavController,
                        StepToStep(currentStepIndex + 1)
                    )
                }
            )

    suspend fun skipToStep(
        stepNavController: StepNavController,
        stepId: String?,
        currentStepIndex: Int
    ): Unit =
        if (stepId == null) end()
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
                    } else {
                        taskConfiguration.onStepLoaded(state().task, it)
                        navigator.navigateTo(stepNavController, StepToStep(skipIndex))
                    }
                }
            )

    suspend fun reschedule(taskNavController: TaskNavController): Unit {

        showLoading(TaskLoading.Reschedule)

        taskConfiguration.reschedule(state().task.id)
            .foldSuspend(
                { setError(unknownError(), TaskError.Reschedule) },
                { close(taskNavController) }
            )

        hideLoading(TaskLoading.Reschedule)

    }

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