package org.fouryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.activity.TaskActivityType
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.decodeBase64ImageFx
import org.fouryouandme.core.ext.mapNotNull

@JsonApi(type = "activity")
class TaskActivityResponse(
    title: String? = null,
    description: String? = null,
    repeatEvery: Int? = null,
    cardColor: String? = null,
    button: String? = null,
    @field:Json(name = "activity_type") val activityType: String? = null,
    @field:Json(name = "image") val image: String? = null

) : ActivityDataResponse(title, description, repeatEvery, cardColor, button) {

    suspend fun toTaskActivity(taskId: String): TaskActivity =
        TaskActivity(
            taskId,
            title,
            description,
            button,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            image?.decodeBase64ImageFx()?.orNull(),
            activityType?.let { TaskActivityType.fromType(it) }
        )
}