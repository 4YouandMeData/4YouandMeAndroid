package org.fouryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.activity.TaskActivityType
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.decodeBase64ImageFx
import org.fouryouandme.core.ext.mapNotNull

@JsonApi(type = "survey")
class SurveyActivityResponse(
    title: String? = null,
    description: String? = null,
    cardColor: String? = null,
    @field:Json(name = "image") val image: String? = null
) : ActivityDataResponse(title, description, null, cardColor) {

    suspend fun toTaskActivity(surveyId: String): TaskActivity =
        TaskActivity(
            surveyId,
            title,
            description,
            null,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            image?.decodeBase64ImageFx()?.orNull(),
            TaskActivityType.Survey
        )

}