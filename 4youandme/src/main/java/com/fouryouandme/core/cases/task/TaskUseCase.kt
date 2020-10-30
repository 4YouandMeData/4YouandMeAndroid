package com.fouryouandme.core.cases.task

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.TaskModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.task.TaskRepository.attachVideo
import com.fouryouandme.core.cases.task.TaskRepository.fetchTask
import com.fouryouandme.core.cases.task.TaskRepository.fetchTasks
import com.fouryouandme.core.cases.task.TaskRepository.uploadFitnessTask
import com.fouryouandme.core.cases.task.TaskRepository.uploadGaitTask
import com.fouryouandme.core.cases.task.TaskRepository.uploadQuickActivity
import com.fouryouandme.core.cases.task.TaskRepository.uploadSurvey
import com.fouryouandme.core.data.api.task.request.FitnessUpdateRequest
import com.fouryouandme.core.data.api.task.request.GaitUpdateRequest
import com.fouryouandme.core.data.api.task.request.SurveyUpdateRequest
import com.fouryouandme.core.entity.task.Task
import java.io.File

object TaskUseCase {

    suspend fun TaskModule.getTask(taskId: String): Either<FourYouAndMeError, Task?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchTask(it, taskId) }

    suspend fun TaskModule.getTasks(): Either<FourYouAndMeError, List<Task>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchTasks(it) }

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

    suspend fun TaskModule.updateFitnessTask(
        taskId: String,
        result: FitnessUpdateRequest
    ): Either<FourYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadFitnessTask(it, taskId, result) }

    suspend fun TaskModule.updateQuickActivity(
        taskId: String,
        answerId: Int
    ): Either<FourYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadQuickActivity(it, taskId, answerId) }

    suspend fun TaskModule.updateSurvey(
        taskId: String,
        result: SurveyUpdateRequest
    ): Either<FourYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadSurvey(it, taskId, result) }

}