package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.*
import com.foryouandme.core.arch.error.unknownError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.foryouandme.core.cases.survey.SurveyUseCase.getSurvey
import com.foryouandme.core.cases.task.TaskUseCase.getTask
import com.foryouandme.core.entity.activity.TaskActivity
import com.foryouandme.core.entity.activity.TaskActivityType
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.core.ext.web.CamCogInterface
import com.foryouandme.core.ext.web.asIntegrationCookies
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskConfiguration
import com.foryouandme.researchkit.task.TaskHandleResult
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.squareup.moshi.Moshi

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
    override suspend fun build(id: String, data: Map<String, String>): Task? {

        val config =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst).orNull()

        val task =
            taskModule.getTask(id)
                .nullToError()
                .map { it.activity as? TaskActivity }
                .orNull()

        return mapNotNull(config, task)
            ?.let {

                when (it.b.activityType) {
                    TaskActivityType.VideoDiary ->
                        FYAMVideoDiaryTask(
                            id,
                            it.a,
                            imageConfiguration,
                            it.b.welcomePage,
                            it.b.successPage,
                        )
                    TaskActivityType.GaitTask ->
                        FYAMGaitTask(
                            id,
                            it.a,
                            imageConfiguration,
                            it.b.welcomePage,
                            it.b.successPage,
                            moshi
                        )
                    TaskActivityType.WalkTask ->
                        FYAMFitnessTask(
                            id,
                            it.a,
                            imageConfiguration,
                            it.b.welcomePage,
                            it.b.successPage,
                            moshi
                        )
                    TaskActivityType.CamCogPvt,
                    TaskActivityType.CamCogNbx,
                    TaskActivityType.CamCogEbt ->
                        authModule.getToken(CachePolicy.MemoryFirst)
                            .orNull()
                            ?.asIntegrationCookies()
                            ?.let { token ->

                                val camCogUrl =
                                    "https://api-4youandme-staging.balzo.eu/camcog/tasks/$id"

                                buildCamCog(
                                    id,
                                    it.a,
                                    imageConfiguration,
                                    camCogUrl,
                                    token,
                                    CamCogInterface()
                                )

                            }
                    TaskActivityType.Survey ->
                        surveyModule.getSurvey(it.b.activityId).orNull()
                            ?.let { survey ->
                                buildSurvey(
                                    id,
                                    it.a,
                                    imageConfiguration,
                                    survey,
                                    it.b.welcomePage,
                                    it.b.successPage
                                )
                            }
                    null -> null
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

}