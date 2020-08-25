package org.fouryouandme.core.data.api.common.response.activity

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.configuration.HEXGradient

@JsonApi(type = "activity")
class TaskActivityResponse(
    title: String? = null,
    description: String? = null,
    repeatEvery: Int? = null,
    startColor: String? = null,
    endColor: String? = null,
    button: String? = null,
    @field:Json(name = "activity_type") val activityType: String? = null,
    @field:Json(name = "image") val image: String? = null

) : ActivityDataResponse(title, description, repeatEvery, startColor, endColor, button) {

    fun toTaskActivity(): TaskActivity =
        TaskActivity(
            id,
            title.toOption(),
            description.toOption(),
            button.toOption(),
            Option.fx { HEXGradient(!startColor.toOption(), !endColor.toOption()) },
            image.toOption(),
            activityType.toOption()
        )
}