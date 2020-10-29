package org.fouryouandme.core.cases.task

import arrow.core.Either
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.task.request.*
import org.fouryouandme.core.data.api.task.response.toTaskItems
import org.fouryouandme.core.entity.task.Task
import java.io.File


object TaskRepository {

    internal suspend fun TaskModule.fetchTask(
        token: String,
        taskId: String
    ): Either<FourYouAndMeError, Task?> =

        errorModule.unwrapToEither { api.getTask(token, taskId) }
            .map { it.toTask() }

    internal suspend fun TaskModule.fetchTasks(
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

    internal suspend fun TaskModule.uploadFitnessTask(
        token: String,
        taskId: String,
        result: FitnessUpdateRequest
    ): Either<FourYouAndMeError, Unit> =
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
    ): Either<FourYouAndMeError, Unit> =
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
    ): Either<FourYouAndMeError, Unit> =
        errorModule.unwrapToEither {
            api.updateSurvey(
                token,
                taskId,
                TaskResultRequest(result)
            )
        }
}