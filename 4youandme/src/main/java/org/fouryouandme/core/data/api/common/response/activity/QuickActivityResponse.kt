package org.fouryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi

@JsonApi(type = "quick_activity")
class QuickActivityResponse(
    title: String? = null,
    description: String? = null,
    @field:Json(name = "quick_activity_options") val options: HasMany<QuickActivityOptionResponse>? = null
) : ActivityDataResponse(title, description)