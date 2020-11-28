package com.foryouandme.data.repository.task

import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.data.api.task.request.FitnessUpdateRequest
import com.foryouandme.core.data.api.task.request.GaitUpdateRequest
import com.foryouandme.core.data.api.task.request.SurveyUpdateRequest
import com.foryouandme.core.entity.task.Task
import com.foryouandme.domain.usecase.task.TaskRepository
import java.io.File

class TaskRepositoryImpl: TaskRepository {

    override suspend fun getTask(taskId: String): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.getTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.attachVideo(taskId: String, file: File) {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.updateGaitTask(taskId: String, result: GaitUpdateRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.updateFitnessTask(
        taskId: String,
        result: FitnessUpdateRequest
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.updateQuickActivity(taskId: String, answerId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.updateSurvey(taskId: String, result: SurveyUpdateRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun TaskModule.reschedule(taskId: String) {
        TODO("Not yet implemented")
    }

}