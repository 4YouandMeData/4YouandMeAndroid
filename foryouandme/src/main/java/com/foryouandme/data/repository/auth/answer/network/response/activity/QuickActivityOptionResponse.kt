package com.foryouandme.data.repository.auth.answer.network.response.activity

import com.foryouandme.core.ext.decodeBase64Image
import com.foryouandme.entity.activity.QuickActivityAnswer
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

    fun toQuickActivityAnswer(): QuickActivityAnswer =
        QuickActivityAnswer(
            id,
            label,
            image?.decodeBase64Image(),
            selectedImage?.decodeBase64Image()
        )
}