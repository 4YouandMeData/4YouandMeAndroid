package com.foryouandme.core.data.api.common.response.activity

import com.foryouandme.core.entity.activity.QuickActivity
import com.foryouandme.core.entity.activity.QuickActivityAnswer
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.ext.mapNotNull
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi

@JsonApi(type = "quick_activity")
class QuickActivityResponse(
    title: String? = null,
    description: String? = null,
    repeatEvery: Int? = null,
    cardColor: String? = null,
    button: String? = null,
    @field:Json(name = "quick_activity_options")
    val options: HasMany<QuickActivityOptionResponse>? = null
) : ActivityDataResponse(
    title,
    description,
    repeatEvery,
    cardColor,
    null,
    null,
    button
) {

    suspend fun toQuickActivity(id: String): QuickActivity =
        QuickActivity(
            id,
            title,
            description,
            button,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            getAnswer(1),
            getAnswer(2),
            getAnswer(3),
            getAnswer(4),
            getAnswer(5),
            getAnswer(6)
        )


    private suspend fun getAnswer(position: Int): QuickActivityAnswer? =
        options?.get(document)
            ?.firstOrNull { it.position == position }?.toQuickActivityAnswer()

}