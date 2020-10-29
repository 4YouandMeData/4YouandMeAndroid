package org.fouryouandme.core.data.api.task

import okhttp3.MultipartBody
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.task.request.*
import org.fouryouandme.core.data.api.task.response.TaskResponse
import retrofit2.http.*

interface TaskApi {

    @GET("api/v1/tasks/{id}")
    suspend fun getTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") id: String
    ): TaskResponse

    @GET("api/v1/tasks")
    suspend fun getTasks(
        @Header(Headers.AUTH) token: String
    ): Array<TaskResponse>

    @Multipart
    @PATCH("/api/v1/tasks/{id}/attach")
    suspend fun attach(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Part file: MultipartBody.Part
    ): Unit

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateGaitTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<GaitUpdateRequest>

    ): Unit

    @PATCH("api/v1/tasks/{id}")
    suspend fun updateFitnessTask(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Body request: TaskResultRequest<FitnessUpdateRequest>

    ): Unit

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

    ): Unit

}

