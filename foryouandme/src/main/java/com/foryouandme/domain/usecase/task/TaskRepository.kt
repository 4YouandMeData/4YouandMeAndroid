package com.foryouandme.domain.usecase.task

import com.foryouandme.entity.order.Order
import com.foryouandme.entity.survey.SurveyAnswerUpdate
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

    suspend fun updateGaitTask(
        token: String,
        taskId: String,
        outboundDeviceMotion: String,
        outboundAccelerometer: String,
        outboundPedometer: String,
        returnDeviceMotion: String,
        returnAccelerometer: String,
        returnPedometer: String,
        restDeviceMotion: String,
        restAccelerometer: String,
    )

    suspend fun updateFitnessTask(
        token: String,
        taskId: String,
        walkDeviceMotion: String,
        walkAccelerometer: String,
        walkPedometer: String,
        sitDeviceMotion: String,
        sitAccelerometer: String,
    )

    suspend fun updateQuickActivity(token: String, taskId: String, answerId: Int)

    suspend fun updateSurvey(token: String, taskId: String, answers: List<SurveyAnswerUpdate>)

    suspend fun reschedule(token: String, taskId: String)

}