package org.fouryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json

class ActivityResponse(
    title: String? = null,
    description: String? = null,
    repeatEvery: Int? = null,
    @field:Json(name = "activity_type") val activityType: String? = null,
    @field:Json(name = "image") val image: String? = null

) : ActivityDataResponse(title, description, repeatEvery)