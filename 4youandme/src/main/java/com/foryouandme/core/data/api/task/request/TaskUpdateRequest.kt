package com.foryouandme.core.data.api.task.request

import com.squareup.moshi.Json

data class TaskUpdateRequest<T>(
    @Json(name = "task") val taskResultRequest: TaskResultRequest<T>
)

data class TaskResultRequest<T>(
    @Json(name = "result") val result: T
)