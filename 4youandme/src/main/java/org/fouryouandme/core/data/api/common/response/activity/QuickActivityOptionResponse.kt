package org.fouryouandme.core.data.api.common.response.activity

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "quick_activity_option")
data class QuickActivityOptionResponse(
    @Json(name = "label") val label: String? = null,
    @Json(name = "position") val position: Int? = null
) : Resource()