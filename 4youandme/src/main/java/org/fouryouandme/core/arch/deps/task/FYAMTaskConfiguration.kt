package org.fouryouandme.core.arch.deps.task

import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskConfiguration
import org.fouryouandme.researchkit.task.TaskHandleResult
import org.fouryouandme.researchkit.task.TaskIdentifiers

class FYAMTaskConfiguration(
    private val configurationModule: ConfigurationModule,
    private val imageConfiguration: ImageConfiguration,
    private val moshi: Moshi,
    private val taskModule: TaskModule
) : TaskConfiguration() {

    override suspend fun build(type: String, id: String): Task? {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst).orNull()

        return configuration?.let {

            when (type) {
                TaskIdentifiers.VIDEO_DIARY ->
                    buildVideoDiary(id, it, imageConfiguration)
                TaskIdentifiers.GAIT ->
                    buildGait(id, configuration, imageConfiguration, moshi)
                TaskIdentifiers.FITNESS ->
                    buildFitness(id, configuration, imageConfiguration, moshi)
                else -> null

            }

        }
    }

    override suspend fun handleTaskResult(
        result: TaskResult,
        type: String,
        id: String
    ): TaskHandleResult =

        when (type) {
            TaskIdentifiers.VIDEO_DIARY -> TaskHandleResult.Handled
            TaskIdentifiers.GAIT -> sendGaitData(taskModule, id, result)
            TaskIdentifiers.FITNESS -> TaskHandleResult.Handled
            else -> TaskHandleResult.Error(unknownError().message)

        }

}