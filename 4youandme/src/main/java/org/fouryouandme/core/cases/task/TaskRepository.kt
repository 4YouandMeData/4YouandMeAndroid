package org.fouryouandme.core.cases.task

import arrow.core.Either
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.task.request.GaitUpdateRequest
import org.fouryouandme.core.data.api.task.request.TaskResultRequest
import org.fouryouandme.core.data.api.task.response.toTaskItems
import org.fouryouandme.core.entity.task.Task
import java.io.File


object TaskRepository {

    internal suspend fun TaskModule.getTasks(
        token: String
    ): Either<FourYouAndMeError, List<Task>> =

        errorModule.unwrapToEither { api.getTasks(token) }
            .map { it.toTaskItems() }

    internal suspend fun TaskModule.attachVideo(
        token: String,
        taskId: String,
        file: File
    ): Either<FourYouAndMeError, Unit> {

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
    ): Either<FourYouAndMeError, Unit> =
        errorModule.unwrapToEither { api.updateGaitTask(token, taskId, TaskResultRequest(result)) }
}