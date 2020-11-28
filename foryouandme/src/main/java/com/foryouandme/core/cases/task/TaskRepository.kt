package com.foryouandme.core.cases.task

import arrow.core.Either
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.data.api.task.request.*
import com.foryouandme.core.data.api.task.response.toTaskItems
import com.foryouandme.core.entity.task.Task
import com.foryouandme.data.network.Order
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


object TaskRepository {

    internal suspend fun TaskModule.fetchTask(
        token: String,
        taskId: String,
    ): Either<ForYouAndMeError, Task?> =

        errorModule.unwrapToEither { api.getTask(token, taskId) }.map { it.toTask() }

    internal suspend fun TaskModule.fetchTasks(
        token: String,
        order: Order,
        page: Int,
        pageSize: Int
    ): Either<ForYouAndMeError, List<Task>> =

        errorModule.unwrapToEither {

            api.getTasks(
                token,
                true,
                true,
                order.value,
                page,
                pageSize
            )

        }.map { it.toTaskItems() }

    internal suspend fun TaskModule.attachVideo(
        token: String,
        taskId: String,
        file: File
    ): Either<ForYouAndMeError, Unit> {

        // create RequestBody instance from file
        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "task[attachment]",
                "VideoDiary.mp4",
                requestFile
            )

        return errorModule.unwrapToEither { api.attach(token, taskId, body) }

    }

    internal suspend fun TaskModule.uploadGaitTask(
        token: String,
        taskId: String,
        result: GaitUpdateRequest
    ): Either<ForYouAndMeError, Unit> =
        errorModule.unwrapToEither { api.updateGaitTask(token, taskId, TaskResultRequest(result)) }

    internal suspend fun TaskModule.uploadFitnessTask(
        token: String,
        taskId: String,
        result: FitnessUpdateRequest
    ): Either<ForYouAndMeError, Unit> =
        errorModule.unwrapToEither {
            api.updateFitnessTask(
                token,
                taskId,
                TaskResultRequest(result)
            )
        }

    internal suspend fun TaskModule.uploadQuickActivity(
        token: String,
        taskId: String,
        answerId: Int
    ): Either<ForYouAndMeError, Unit> =
        errorModule.unwrapToEither {
            api.updateQuickActivity(
                token,
                taskId,
                TaskResultRequest(QuickActivityUpdateRequest(answerId))
            )
        }

    internal suspend fun TaskModule.uploadSurvey(
        token: String,
        taskId: String,
        result: SurveyUpdateRequest
    ): Either<ForYouAndMeError, Unit> =
        errorModule.unwrapToEither {
            api.updateSurvey(
                token,
                taskId,
                TaskResultRequest(result)
            )
        }

    internal suspend fun TaskModule.reschedule(
        token: String,
        taskId: String
    ): Either<ForYouAndMeError, Unit> =
        suspend { api.reschedule(token, taskId) }
            .let { errorModule.unwrapToEither(it) }
}