package org.fouryouandme.core.data.api.task.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "task")
data class TaskResponse(
    @Json(name = "from") val from: String? = null,
    @Json(name = "to") val to: String? = null
) : Resource()