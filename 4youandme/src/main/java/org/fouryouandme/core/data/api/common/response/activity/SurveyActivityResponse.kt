package org.fouryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi

@JsonApi(type = "survey")
class SurveyActivityResponse(
    title: String? = null,
    description: String? = null,
    @field:Json(name = "image") val image: String? = null
) : ActivityDataResponse(title, description)