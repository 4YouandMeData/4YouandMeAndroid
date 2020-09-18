package org.fouryouandme.tasks

import androidx.navigation.NavController
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.ext.foldSuspend
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepNavController
import org.fouryouandme.researchkit.task.ETaskType
import org.fouryouandme.researchkit.task.Task

class TaskViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<
        ForIO,
        TaskState,
        TaskStateUpdate,
        TaskError,
        TaskLoading>
    (navigator = navigator, runtime = runtime) {

    /* --- initialization --- */

    suspend fun initialize(type: ETaskType): Unit {

        showLoadingFx(TaskLoading.Initialization)

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)


        configuration.fold(
            { setErrorFx(it, TaskError.Initialization) },
            { config ->

                val task =
                    Task.byType(
                        type,
                        config,
                        runtime.injector.imageConfiguration,
                        runtime.injector.moshi
                    )

                setStateFx(
                    TaskState(
                        configuration = config,
                        task = task,
                        isCancelled = false,
                        result = TaskResult(task.identifier, emptyMap())
                    )
                ) { TaskStateUpdate.Initialization(it.configuration, task) }
            }
        )

        hideLoadingFx(TaskLoading.Initialization)

    }

    /* --- state --- */

    suspend fun cancel(): Unit =
        setStateFx(
            state().copy(isCancelled = true)
        ) { TaskStateUpdate.Cancelled(it.isCancelled) }

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