package org.fouryouandme.core.cases.task

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.task.TaskRepository.attachVideo
import org.fouryouandme.core.cases.task.TaskRepository.getTasks
import org.fouryouandme.core.cases.task.TaskRepository.uploadGaitTask
import org.fouryouandme.core.data.api.task.request.GaitUpdateRequest
import org.fouryouandme.core.entity.task.Task
import java.io.File

object TaskUseCase {

    suspend fun TaskModule.getTasks(): Either<FourYouAndMeError, List<Task>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { getTasks(it) }

    suspend fun TaskModule.attachVideo(
        taskId: String,
        file: File
    ): Either<FourYouAndMeError, Unit> =

        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { attachVideo(it, taskId, file) }

    suspend fun TaskModule.updateGaitTask(
        taskId: String,
        result: GaitUpdateRequest
    ): Either<FourYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadGaitTask(it, taskId, result) }

}