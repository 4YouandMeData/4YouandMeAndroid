package org.fouryouandme.core.data.api.common.response.activity

import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.activity.QuickActivityAnswer

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
            label.toOption(),
            image.toOption(),
            selectedImage.toOption()
        )
}