package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.survey.GetSurveyUseCase
import com.foryouandme.domain.usecase.task.*
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.activity.TaskActivityType
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.survey.SurveyAnswerUpdate
import com.foryouandme.entity.task.result.holepeg.toHolePegResult
import com.foryouandme.entity.task.result.reaction.toReactionTimeResult
import com.foryouandme.entity.task.result.trailmaking.toTrailMakingResult
import com.foryouandme.researchkit.result.*
import com.foryouandme.researchkit.result.fitness.toFitnessResult
import com.foryouandme.researchkit.result.gait.toGaitResult
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.video.VideoStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskConfiguration
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.squareup.moshi.Moshi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FYAMTaskConfiguration @Inject constructor(
    private val environment: Environment,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val getSurveyUseCase: GetSurveyUseCase,
    private val sendGaitTaskUseCase: SendGaitTaskUseCase,
    private val sendFitnessTaskUseCase: SendFitnessTaskUseCase,
    private val sendReactionTimeUseCase: SendReactionTimeUseCase,
    private val sendTrailMakingUseCase: SendTrailMakingUseCase,
    private val sendHolePegUseCase: SendHolePegUseCase,
    private val sendSurveyTaskUseCase: SendSurveyTaskUseCase,
    private val rescheduleTaskUseCase: RescheduleTaskUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    private val imageConfiguration: ImageConfiguration,
    private val moshi: Moshi,
) : TaskConfiguration() {

    override suspend fun build(id: String, data: Map<String, String>): Task? =
        coroutineScope {

            val config = async { getConfigurationUseCase(Policy.LocalFirst) }
            val task = async { getTaskUseCase(id)!!.let { it.activity as? TaskActivity }!! }

            build(id, config.await(), task.await())

        }

    private suspend fun build(
        id: String,
        configuration: Configuration,
        task: TaskActivity
    ): Task? =
        /*when (task.activityType) {
            TaskActivityType.VideoDiary ->
                FYAMVideoDiaryTask(
                    id,
                    configuration,
                    imageConfiguration,
                    task.pages,
                    task.welcomePage,
                    task.successPage,
                    task.reschedule
                )
            TaskActivityType.GaitTask ->
                FYAMGaitTask(
                    id,
                    configuration,
                    imageConfiguration,
                    task.pages,
                    task.welcomePage,
                    task.successPage,
                    task.reschedule,
                    moshi
                )
            TaskActivityType.WalkTask ->
                FYAMFitnessTask(
                    id,
                    configuration,
                    imageConfiguration,
                    task.pages,
                    task.welcomePage,
                    task.successPage,
                    task.reschedule,
                    moshi
                )
            TaskActivityType.TrailMaking ->
                FYAMTrailMakingTask(
                    id,
                    null,
                    ETrailMakingType.NUMBER_AND_LETTER,
                    configuration,
                    imageConfiguration,
                    task.pages,
                    task.welcomePage,
                    task.successPage,
                    task.reschedule
                )
            TaskActivityType.ReactionTime ->
                FYAMReactionTimeTask(
                    id,
                    null,
                    10,
                    3,
                    3,
                    3,
                    configuration,
                    imageConfiguration,
                    task.pages,
                    task.welcomePage,
                    task.successPage,
                    task.reschedule
                )
            TaskActivityType.CamCogPvt,
            TaskActivityType.CamCogNbx,
            TaskActivityType.CamCogEbt ->
                getTokenUseCase()
                    .asIntegrationCookies()
                    .let { token ->

                        val camCogUrl =
                            "${environment.getApiBaseUrl()}/camcog/tasks/$id"

                        FYAMCamCogTask(
                            id,
                            configuration,
                            imageConfiguration,
                            task.pages,
                            task.welcomePage,
                            task.successPage,
                            task.reschedule,
                            camCogUrl,
                            token,
                            CamCogInterface()
                        )

                    }
            TaskActivityType.Survey -> {
                val survey = getSurveyUseCase(task.activityId)
                buildSurvey(
                    id,
                    configuration,
                    imageConfiguration,
                    survey!!,
                    task.pages,
                    task.welcomePage,
                    task.successPage,
                    task.reschedule
                )
            }
            else -> null
        }*/ FYAMHolePegTask(id, configuration, imageConfiguration)

    override suspend fun handleTaskResult(
        result: TaskResult,
        type: String,
        id: String
    ) {

        when (type) {
            TaskIdentifiers.VIDEO_DIARY -> {
                sendAnalyticsEventUseCase(
                    AnalyticsEvent.ScreenViewed.VideoDiaryComplete,
                    EAnalyticsProvider.ALL
                )
            }
            TaskIdentifiers.GAIT -> {
                // upload data to task api
                val gaitResult = result.toGaitResult()
                if (gaitResult != null)
                    sendGaitTaskUseCase(
                        taskId = id,
                        outboundDeviceMotion = gaitResult.gaitOutbound.deviceMotion,
                        outboundAccelerometer = gaitResult.gaitOutbound.accelerometer,
                        outboundPedometer = gaitResult.gaitOutbound.pedometer,
                        returnDeviceMotion = gaitResult.gaitReturn.deviceMotion,
                        returnAccelerometer = gaitResult.gaitReturn.accelerometer,
                        returnPedometer = gaitResult.gaitReturn.pedometer,
                        restDeviceMotion = gaitResult.gaitRest.deviceMotion,
                        restAccelerometer = gaitResult.gaitRest.accelerometer
                    )
            }
            TaskIdentifiers.FITNESS -> {
                val fitnessResult = result.toFitnessResult()
                if (fitnessResult != null)
                    sendFitnessTaskUseCase(
                        taskId = id,
                        walkDeviceMotion = fitnessResult.fitnessWalkResult.deviceMotion,
                        walkAccelerometer = fitnessResult.fitnessWalkResult.accelerometer,
                        walkPedometer = fitnessResult.fitnessWalkResult.pedometer,
                        sitDeviceMotion = fitnessResult.fitnessSitResult.deviceMotion,
                        sitAccelerometer = fitnessResult.fitnessSitResult.accelerometer
                    )
            }
            TaskIdentifiers.REACTION_TIME -> {
                val reactionTimeResult = result.toReactionTimeResult()
                if (reactionTimeResult != null)
                    sendReactionTimeUseCase(taskId = id, reactionTimeResult = reactionTimeResult)
            }
            TaskIdentifiers.TRAIL_MAKING -> {
                val trailMakingResult = result.toTrailMakingResult()
                if (trailMakingResult != null)
                    sendTrailMakingUseCase(taskId = id, trailMakingResult = trailMakingResult)
            }
            TaskIdentifiers.HOLE_PEG -> {
                val holePegResult = result.toHolePegResult()
                if (holePegResult != null)
                    sendHolePegUseCase(taskId = id, holePegResult = holePegResult)
            }
            TaskActivityType.Survey.typeId -> {

                val answers =
                    result.results.toList().mapNotNull {
                        when (val value = it.second) {
                            is SingleAnswerResult ->
                                SurveyAnswerUpdate(value.questionId, value.answer)
                            is SingleStringAnswerResult ->
                                SurveyAnswerUpdate(value.questionId, value.answer)
                            is SingleIntAnswerResult ->
                                SurveyAnswerUpdate(value.questionId, value.answer)
                            is MultipleAnswerResult ->
                                SurveyAnswerUpdate(value.questionId, value.answers)
                            else -> null

                        }
                    }

                sendSurveyTaskUseCase(id, answers)

            }
            else -> throw ForYouAndMeException.Unknown

        }
    }

    override suspend fun reschedule(id: String) {

        rescheduleTaskUseCase(id)

    }

    override suspend fun onStepLoaded(task: Task, step: Step) {
        when (step) {
            is VideoStep ->
                sendAnalyticsEventUseCase(
                    AnalyticsEvent.ScreenViewed.VideoDiary,
                    EAnalyticsProvider.ALL
                )
        }
    }

}