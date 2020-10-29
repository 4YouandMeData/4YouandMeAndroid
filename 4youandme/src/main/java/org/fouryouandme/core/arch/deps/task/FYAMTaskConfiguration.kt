package org.fouryouandme.core.arch.deps.task

import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.*
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.cases.survey.SurveyUseCase.getSurvey
import org.fouryouandme.core.entity.activity.TaskActivityType
import org.fouryouandme.core.ext.mapNotNull
import org.fouryouandme.core.ext.web.CamCogInterface
import org.fouryouandme.core.ext.web.asIntegrationCookies
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskConfiguration
import org.fouryouandme.researchkit.task.TaskHandleResult
import org.fouryouandme.researchkit.task.TaskIdentifiers
import java.util.*

class FYAMTaskConfiguration(
    private val configurationModule: ConfigurationModule,
    private val imageConfiguration: ImageConfiguration,
    private val moshi: Moshi,
    private val taskModule: TaskModule,
    private val surveyModule: SurveyModule,
    private val authModule: AuthModule,
    private val errorModule: ErrorModule
) : TaskConfiguration() {

    // TODO: handle auth error and other error
    override suspend fun build(type: String, id: String, data: Map<String, String>): Task? {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst).orNull()

        val taskActivityId = data[ACTIVITY_ID]

        return mapNotNull(configuration, taskActivityId)
            ?.let { (config, activityId) ->

                when (type) {
                    TaskIdentifiers.VIDEO_DIARY ->
                        buildVideoDiary(id, config, imageConfiguration)
                    TaskIdentifiers.GAIT ->
                        buildGait(id, config, imageConfiguration, moshi)
                    TaskIdentifiers.FITNESS ->
                        buildFitness(id, config, imageConfiguration, moshi)
                    TaskActivityType.Survey.typeId ->
                        surveyModule.getSurvey(activityId).orNull()
                            ?.let { buildSurvey(id, config, imageConfiguration, it) }
                    TaskIdentifiers.CAMCOG ->
                        authModule.getToken(CachePolicy.MemoryFirst)
                            .orNull()
                            ?.asIntegrationCookies()
                            ?.let {

                                buildCamCog(
                                    id,
                                    config,
                                    imageConfiguration,
                                    "https://api-4youandme-staging.balzo.eu/camcog/tasks/$id",
                                    it,
                                    CamCogInterface()
                                )

                            }

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
            TaskIdentifiers.GAIT -> sendGaitData(taskModule, errorModule, id, result)
            TaskIdentifiers.FITNESS -> sendFitnessData(taskModule, errorModule, id, result)
            TaskActivityType.Survey.typeId -> sendSurveyData(taskModule, id, result)
            else -> TaskHandleResult.Error(unknownError().message)

        }


    companion object {

        private const val ACTIVITY_ID: String = "activity_id"

        fun getTaskDataMap(activityId: String): HashMap<String, String> =
            hashMapOf(ACTIVITY_ID to activityId)

    }

}