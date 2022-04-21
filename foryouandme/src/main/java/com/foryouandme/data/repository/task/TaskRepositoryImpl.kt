package com.foryouandme.data.repository.task

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.datasource.network.value
import com.foryouandme.data.repository.task.network.TaskApi
import com.foryouandme.data.repository.task.network.request.*
import com.foryouandme.domain.usecase.task.TaskRepository
import com.foryouandme.entity.order.Order
import com.foryouandme.entity.survey.SurveyAnswerUpdate
import com.foryouandme.entity.task.Task
import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition
import com.foryouandme.entity.task.result.holepeg.HolePegResult
import com.foryouandme.entity.task.result.reaction.ReactionTimeResult
import com.foryouandme.entity.task.result.trailmaking.TrailMakingResult
import com.foryouandme.researchkit.result.AnswerResult
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.toPagedList
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : TaskRepository {

    override suspend fun getTask(token: String, taskId: String): Task? =
        authErrorInterceptor.execute { api.getTask(token, taskId) }.toTask()

    override suspend fun getTasks(
        token: String,
        order: Order,
        page: Int,
        pageSize: Int
    ): PagedList<Task> {

        val taskResponse =
            authErrorInterceptor.execute {

                api.getTasks(
                    token = token,
                    active = true,
                    notCompleted = true,
                    order = order.value,
                    page = page,
                    pageSize = pageSize,
                    urlImagesEncoding = 1
                )

            }.mapNotNull { it.toTask() }

        return taskResponse.toPagedList(page, taskResponse.size < pageSize)

    }

    override suspend fun attachVideo(token: String, taskId: String, file: File) {

        authErrorInterceptor.execute {
            api.updateVideoTask(
                token,
                taskId,
                TaskResultRequest(EmptyUpdateRequest())
            )
        }

        // create RequestBody instance from file
        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "task[attachment]",
                "VideoDiary.mp4",
                requestFile
            )

        return authErrorInterceptor.execute { api.attach(token, taskId, body) }


    }

    override suspend fun updateGaitTask(
        token: String,
        taskId: String,
        outboundDeviceMotion: String,
        outboundAccelerometer: String,
        outboundPedometer: String,
        returnDeviceMotion: String,
        returnAccelerometer: String,
        returnPedometer: String,
        restDeviceMotion: String,
        restAccelerometer: String,
    ) {

        authErrorInterceptor.execute {
            api.updateGaitTask(
                token,
                taskId,
                TaskResultRequest(
                    GaitUpdateRequest(
                        GaitOutboundRequest(
                            outboundDeviceMotion,
                            outboundAccelerometer,
                            outboundPedometer
                        ),
                        GaitReturnRequest(
                            returnDeviceMotion,
                            restAccelerometer,
                            returnPedometer
                        ),
                        GaitRestRequest(
                            restDeviceMotion,
                            restAccelerometer
                        )
                    )
                )
            )
        }

    }

    override suspend fun updateFitnessTask(
        token: String,
        taskId: String,
        walkDeviceMotion: String,
        walkAccelerometer: String,
        walkPedometer: String,
        sitDeviceMotion: String,
        sitAccelerometer: String,
    ) {

        authErrorInterceptor.execute {

            api.updateFitnessTask(
                token,
                taskId,
                TaskResultRequest(
                    FitnessUpdateRequest(
                        FitnessWalkRequest(walkDeviceMotion, walkAccelerometer, walkPedometer),
                        FitnessSitRequest(sitDeviceMotion, sitAccelerometer)
                    )
                )
            )

        }

    }

    override suspend fun updateReactionTimeTask(
        token: String,
        taskId: String,
        result: ReactionTimeResult
    ) {

        authErrorInterceptor.execute {

            api.updateReactionTimeTask(
                token,
                taskId,
                TaskResultRequest(
                    ReactionTimeUpdateRequest(
                        result.startDate.toInstant().toEpochMilli(),
                        result.attempts
                            .mapNotNull { it.toJsonResult() }
                            .map {
                                ReactionTimeAttemptRequest(
                                    it.json,
                                    it.startDate.toInstant().toEpochMilli()
                                )
                            },
                        result.endDate.toInstant().toEpochMilli()
                    )
                )
            )

        }

    }

    override suspend fun updateTrailMakingTask(
        token: String,
        taskId: String,
        result: TrailMakingResult
    ) {
        authErrorInterceptor.execute {

            api.updateTrailMakingTimeTask(
                token,
                taskId,
                TaskResultRequest(
                    TrailMakingUpdateRequest(
                        result.startDate.toInstant().toEpochMilli(),
                        result.numberOfErrors,
                        result.taps.map {
                            TrailMakingTapUpdateRequest(it.index, it.timestamp, it.incorrect)
                        },
                        result.endDate.toInstant().toEpochMilli()
                    )
                )
            )

        }
    }

    override suspend fun updateHolePegTask(
        token: String,
        taskId: String,
        result: HolePegResult
    ) {
        authErrorInterceptor.execute {

            api.updateHolePegTask(
                token = token,
                taskId = taskId,
                request = TaskResultRequest(
                    HolePegUpdateRequest(
                        steps = result.steps.map { attempt ->
                            HolePegStepUpdateRequest(
                                pegs = attempt.pegs.map {
                                    PegUpdateRequest(
                                        startTime = it.startDate.toInstant().toEpochMilli(),
                                        endTime = it.endDate.toInstant().toEpochMilli(),
                                        distance = it.distance
                                    )
                                },
                                startTime = attempt.startDate.toInstant().toEpochMilli(),
                                endTime = attempt.endDate.toInstant().toEpochMilli(),
                                startPoint = when (attempt.step.start) {
                                    HolePegPointPosition.Center -> "center"
                                    HolePegPointPosition.End -> "right"
                                    HolePegPointPosition.Start -> "left"
                                },
                                targetPoint = when (attempt.step.target) {
                                    HolePegTargetPosition.End -> "right"
                                    HolePegTargetPosition.EndCenter -> "right_center"
                                    HolePegTargetPosition.Start -> "start"
                                    HolePegTargetPosition.StartCenter -> "start_center"
                                },
                                numberOfErrors = attempt.errorCount,
                                numberOfPegs = attempt.totalPegs
                            )
                        },
                        startTime = result.startDate.toInstant().toEpochMilli(),
                        endTime = result.endDate.toInstant().toEpochMilli()
                    )
                )
            )

        }
    }

    override suspend fun updateQuickActivity(token: String, taskId: String, answerId: Int) {

        authErrorInterceptor.execute {

            api.updateQuickActivity(
                token,
                taskId,
                TaskResultRequest(QuickActivityUpdateRequest(answerId))
            )

        }

    }

    override suspend fun updateSurvey(
        token: String,
        taskId: String,
        answers: List<SurveyAnswerUpdate>
    ) {

        authErrorInterceptor.execute {

            api.updateSurvey(
                token,
                taskId,
                TaskResultRequest(
                    SurveyUpdateRequest(
                        answers.map {
                            AnswerUpdateRequest(
                                it.questionId,
                                when (it.answer) {
                                    is AnswerResult ->
                                        AnswerRequest(it.answer.answerId, it.answer.answerText)
                                    else ->
                                        it.answer
                                }
                            )
                        }
                    )
                )
            )

        }

    }

    override suspend fun reschedule(token: String, taskId: String) {

        authErrorInterceptor.execute { api.reschedule(token, taskId) }

    }

}