package org.fouryouandme.core.data.api.task.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.data.api.common.response.activity.ActivityDataResponse

@JsonApi(type = "task")
data class TaskResponse(
    @field:Json(name = "from") val from: String? = null,
    @field:Json(name = "to") val to: String? = null,
    @field:Json(name = "schedulable") val activity: HasOne<ActivityDataResponse>? = null
) : Resource()