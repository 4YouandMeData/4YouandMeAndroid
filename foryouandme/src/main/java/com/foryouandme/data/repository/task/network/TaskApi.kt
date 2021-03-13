package com.foryouandme.data.repository.task.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.task.network.request.*
import com.foryouandme.data.repository.task.network.response.TaskResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface TaskApi {

    @GET("api/v1/tasks/{id}")
    suspend fun getTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") id: String
    ): TaskResponse

    @GET("api/v1/tasks")
    suspend fun getTasks(
        @Header(Headers.AUTH) token: String,
        @Query("q[active]") active: Boolean,
        @Query("q[not_completed]") notCompleted: Boolean,
        @Query("q[s]") order: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): Array<TaskResponse>

    @Multipart
    @PATCH("/api/v1/tasks/{id}/attach")
    suspend fun attach(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Part file: MultipartBody.Part
    )

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateGaitTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<GaitUpdateRequest>
    )

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateFitnessTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<FitnessUpdateRequest>
    )

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateReactionTimeTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<ReactionTimeUpdateRequest>
    )

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateTrailMakingTimeTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<TrailMakingUpdateRequest>
    )

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateQuickActivity(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<QuickActivityUpdateRequest>
    )

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateSurvey(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<SurveyUpdateRequest>

    )

    @PATCH("api/v1/tasks/{id}/reschedule")
    suspend fun reschedule(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String

    )

}

