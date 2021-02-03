package com.foryouandme.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.*
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.researchkit.result.StepResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.TaskConfiguration
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

class TaskViewModel @ViewModelInject constructor(
    private val errorFlow: ErrorFlow<TaskError>,
    private val loadingFlow: LoadingFlow<TaskLoading>,
    private val stateUpdateFlow: StateUpdateFlow<TaskStateUpdate>,
    private val navigationFlow: NavigationFlow,
    private val taskConfiguration: TaskConfiguration,
    private val getConfigurationUseCase: GetConfigurationUseCase
) : ViewModel() {

    /* --- state --- */

    var state: TaskState = TaskState()
        private set

    /* --- flow --- */

    val stateUpdate: SharedFlow<TaskStateUpdate> = stateUpdateFlow.stateUpdates
    val loading: SharedFlow<UILoading<TaskLoading>> = loadingFlow.loading
    val error: SharedFlow<UIError<TaskError>> = errorFlow.error
    val navigation: SharedFlow<NavigationAction> = navigationFlow.navigation

    /* --- initialization --- */

    private suspend fun initialize(id: String, data: Map<String, String>) {
        coroutineScope {

            loadingFlow.show(TaskLoading.Initialization)

            val task = async { taskConfiguration.build(id, data)!! }
            val configuration = async { getConfigurationUseCase(Policy.LocalFirst) }

            state =
                state.copy(
                    task = task.await(),
                    result = TaskResult(task.await().type, emptyMap()),
                    configuration = configuration.await()
                )
            stateUpdateFlow.update(TaskStateUpdate.Initialization(task.await()))

            loadingFlow.hide(TaskLoading.Initialization)

        }
    }

    /* --- action --- */

    private suspend fun cancel() {
        state = state.copy(isCancelled = true)
        stateUpdateFlow.update(TaskStateUpdate.Cancelled(true))
    }

    private suspend fun end() {

        loadingFlow.show(TaskLoading.Result)

        val task = state.task
        val result = state.result

        if (task != null && result != null)
            taskConfiguration.handleTaskResult(result, task.type, task.id)

        state = state.copy(isCompleted = true)
        stateUpdateFlow.update(TaskStateUpdate.Completed)

        loadingFlow.hide(TaskLoading.Result)

    }

    private suspend fun addResult(result: StepResult) {

        val update =
            state.result
                ?.results
                ?.toMutableMap()
                ?.also { it[result.identifier] = result }
                ?: emptyMap()

        state =
            state.copy(
                result =
                state.result?.copy(
                    results = update
                )
            )

    }

    /* --- step --- */

    fun getStepByIndex(index: Int): Step? = state.task?.steps?.getOrNull(index)

    private fun getStepById(id: String): Step? =
        state.task?.steps?.firstOrNull { it.identifier == id }

    inline fun <reified T : Step> getStepByIndexAs(index: Int): T? = getStepByIndex(index) as? T

    fun canGoBack(stepIndex: Int): Boolean =
        getStepByIndex(stepIndex - 1)?.back != null

    /* --- navigation --- */

    private suspend fun nextStep(currentStepIndex: Int) {

        val task = state.task
        val step = getStepByIndex(currentStepIndex + 1)

        if (step != null && task != null) {
            taskConfiguration.onStepLoaded(task, step)
            navigationFlow.navigateTo(StepToStep(currentStepIndex + 1))
        } else end()

    }

    private suspend fun skipToStep(
        stepId: String?,
        currentStepIndex: Int
    ) {

        val task = state.task
        val step = stepId?.let { getStepById(it) }

        if (step != null && task != null) {

            val skipIndex = task.steps.indexOf(step)

            if (skipIndex <= currentStepIndex) {
                Timber.tag(TAG)
                    .e("Unable to skip to step $stepId for task ${task.id}, the step index is <= to the index of the current step")
                nextStep(currentStepIndex)
            } else {
                taskConfiguration.onStepLoaded(task, step)
                navigationFlow.navigateTo(StepToStep(skipIndex))
            }

        } else {
            Timber.tag(TAG)
                .e("Unable to skip to step $stepId for task ${state.task?.id}, there is no step with this id")
            nextStep(currentStepIndex)
        }
    }

    private suspend fun reschedule() {

        loadingFlow.show(TaskLoading.Reschedule)

        val task = state.task

        if (task != null)
            taskConfiguration.reschedule(task.id)

        stateUpdateFlow.update(TaskStateUpdate.Rescheduled)

        loadingFlow.hide(TaskLoading.Reschedule)

    }

    /* --- state event --- */

    fun execute(stateEvent: TaskStateEvent) {

        when (stateEvent) {
            is TaskStateEvent.Initialize ->
                errorFlow.launchCatch(viewModelScope, TaskError.Initialization)
                { initialize(stateEvent.id, stateEvent.data) }
            TaskStateEvent.Reschedule ->
                errorFlow.launchCatch(viewModelScope, TaskError.Reschedule)
                { reschedule() }
            TaskStateEvent.End ->
                errorFlow.launchCatch(viewModelScope, TaskError.Result)
                { end() }
            is TaskStateEvent.SkipToStep ->
                viewModelScope.launchSafe {
                    skipToStep(stateEvent.stepId, stateEvent.currentStepIndex)
                }
            is TaskStateEvent.NextStep ->
                viewModelScope.launchSafe {
                    nextStep(stateEvent.currentStepIndex)
                }
        }

    }

    companion object {

        private const val TAG: String = "Research Kit"

    }
}