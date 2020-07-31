package org.fouryouandme.core.data.api.common.response

import com.squareup.moshi.Json

data class QuickActivityResponse(
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "repeat_every") val repeatEvery: Int? = null
)