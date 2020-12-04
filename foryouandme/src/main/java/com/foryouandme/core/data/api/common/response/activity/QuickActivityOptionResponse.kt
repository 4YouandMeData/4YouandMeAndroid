package com.foryouandme.core.data.api.common.response.activity

import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.core.ext.decodeBase64ImageFx
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "quick_activity_option")
data class QuickActivityOptionResponse(
    @field:Json(name = "label") val label: String? = null,
    @field:Json(name = "position") val position: Int? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "selected_image") val selectedImage: String? = null
) : Resource() {

    suspend fun toQuickActivityAnswer(): QuickActivityAnswer =
        QuickActivityAnswer(
            id,
            label,
            image?.decodeBase64ImageFx()?.orNull(),
            selectedImage?.decodeBase64ImageFx()?.orNull()
        )
}