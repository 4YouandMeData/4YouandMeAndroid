package org.fouryouandme.core.cases.task

import arrow.Kind
import arrow.core.Either
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.task.response.toTaskItems
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unwrapToEither
import java.io.File


object TaskRepository {

    internal fun <F> getTasks(
        runtime: Runtime<F>,
        token: String
    ): Kind<F, Either<FourYouAndMeError, List<Task>>> =
        runtime.fx.concurrent {

            !runtime.injector.taskApi.getTasks(token)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.toTaskItems() }

        }

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
}