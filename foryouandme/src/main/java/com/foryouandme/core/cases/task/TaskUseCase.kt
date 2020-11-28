package com.foryouandme.core.cases.task

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.task.TaskRepository.attachVideo
import com.foryouandme.core.cases.task.TaskRepository.fetchTask
import com.foryouandme.core.cases.task.TaskRepository.fetchTasks
import com.foryouandme.core.cases.task.TaskRepository.reschedule
import com.foryouandme.core.cases.task.TaskRepository.uploadFitnessTask
import com.foryouandme.core.cases.task.TaskRepository.uploadGaitTask
import com.foryouandme.core.cases.task.TaskRepository.uploadQuickActivity
import com.foryouandme.core.cases.task.TaskRepository.uploadSurvey
import com.foryouandme.core.data.api.task.request.FitnessUpdateRequest
import com.foryouandme.core.data.api.task.request.GaitUpdateRequest
import com.foryouandme.core.data.api.task.request.SurveyUpdateRequest
import com.foryouandme.core.entity.task.Task
import com.foryouandme.data.network.Order
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.toPagedList
import java.io.File

object TaskUseCase {

    suspend fun TaskModule.getTask(taskId: String): Either<ForYouAndMeError, Task?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchTask(it, taskId) }

    suspend fun TaskModule.getTasks(
        order: Order,
        page: Int,
        pageSize: Int
    ): Either<ForYouAndMeError, PagedList<Task>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchTasks(it, order, page, pageSize) }
            .map { it.toPagedList(page, it.size < pageSize) }

    suspend fun TaskModule.attachVideo(
        taskId: String,
        file: File
    ): Either<ForYouAndMeError, Unit> =

        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { attachVideo(it, taskId, file) }

    suspend fun TaskModule.updateGaitTask(
        taskId: String,
        result: GaitUpdateRequest
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadGaitTask(it, taskId, result) }

    suspend fun TaskModule.updateFitnessTask(
        taskId: String,
        result: FitnessUpdateRequest
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadFitnessTask(it, taskId, result) }

    suspend fun TaskModule.updateQuickActivity(
        taskId: String,
        answerId: Int
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadQuickActivity(it, taskId, answerId) }

    suspend fun TaskModule.updateSurvey(
        taskId: String,
        result: SurveyUpdateRequest
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { uploadSurvey(it, taskId, result) }

    suspend fun TaskModule.reschedule(taskId: String): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { reschedule(it, taskId) }

}