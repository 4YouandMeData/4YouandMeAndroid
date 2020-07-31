package org.fouryouandme.core.data.api.task

import arrow.integrations.retrofit.adapter.CallK
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.task.response.TaskResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface TaskApi {

    @GET("api/v1/tasks")
    fun getTasks(
        @Header(Headers.AUTH) token: String
    ): CallK<Array<TaskResponse>>

}

