package com.foryouandme.domain.usecase.task

import com.foryouandme.entity.order.Order
import com.foryouandme.data.repository.task.network.request.FitnessUpdateRequest
import com.foryouandme.data.repository.task.network.request.GaitUpdateRequest
import com.foryouandme.data.repository.task.network.request.SurveyUpdateRequest
import com.foryouandme.entity.task.Task
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import java.io.File

interface TaskRepository {

    suspend fun getTask(token: String, taskId: String): Task?

    suspend fun getTasks(
        token: String,
        order: Order,
        page: Int,
        pageSize: Int
    ): PagedList<Task>

    suspend fun attachVideo(token: String, taskId: String, file: File)

    suspend fun updateGaitTask(token: String, taskId: String, result: GaitUpdateRequest)

    suspend fun updateFitnessTask(token: String, taskId: String, result: FitnessUpdateRequest)

    suspend fun updateQuickActivity(token: String, taskId: String, answerId: Int)

    suspend fun updateSurvey(token: String, taskId: String, result: SurveyUpdateRequest)

    suspend fun reschedule(token: String, taskId: String)

}