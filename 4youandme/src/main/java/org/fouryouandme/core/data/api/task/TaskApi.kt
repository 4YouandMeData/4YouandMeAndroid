package org.fouryouandme.core.data.api.task

import arrow.integrations.retrofit.adapter.CallK
import okhttp3.MultipartBody
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.task.response.TaskResponse
import retrofit2.http.*

interface TaskApi {

    @GET("api/v1/tasks")
    fun getTasks(
        @Header(Headers.AUTH) token: String
    ): CallK<Array<TaskResponse>>


    @Multipart
    @PATCH("/api/v1/tasks/{id}/attach")
    suspend fun attach(
        @Header(Headers.AUTH) token: String,
        @Path("id") taskId: String,
        @Part file: MultipartBody.Part
    ): Unit

}

