package com.foryouandme.domain.usecase.task

import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.data.api.task.request.FitnessUpdateRequest
import com.foryouandme.core.data.api.task.request.GaitUpdateRequest
import com.foryouandme.core.data.api.task.request.SurveyUpdateRequest
import com.foryouandme.core.entity.task.Task
import java.io.File

interface TaskRepository {

    suspend fun getTask(taskId: String): Task?

    suspend fun TaskModule.getTasks(): List<Task>

    suspend fun TaskModule.attachVideo(taskId: String, file: File): Unit

    suspend fun TaskModule.updateGaitTask(taskId: String, result: GaitUpdateRequest): Unit

    suspend fun TaskModule.updateFitnessTask(taskId: String, result: FitnessUpdateRequest): Unit

    suspend fun TaskModule.updateQuickActivity(taskId: String, answerId: Int): Unit

    suspend fun TaskModule.updateSurvey(taskId: String, result: SurveyUpdateRequest): Unit

    suspend fun TaskModule.reschedule(taskId: String): Unit

}