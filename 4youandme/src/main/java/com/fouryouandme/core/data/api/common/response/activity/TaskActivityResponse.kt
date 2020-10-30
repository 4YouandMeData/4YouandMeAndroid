package com.fouryouandme.core.data.api.common.response.activity

import com.fouryouandme.core.data.api.common.response.PageResponse
import com.fouryouandme.core.entity.activity.TaskActivity
import com.fouryouandme.core.entity.activity.TaskActivityType
import com.fouryouandme.core.entity.configuration.HEXGradient
import com.fouryouandme.core.ext.decodeBase64ImageFx
import com.fouryouandme.core.ext.mapNotNull
import com.squareup.moshi.Json
import moe.banana.jsonapi2.Document
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi

@JsonApi(type = "activity")
class TaskActivityResponse(
    title: String? = null,
    description: String? = null,
    repeatEvery: Int? = null,
    cardColor: String? = null,
    button: String? = null,
    @field:Json(name = "activity_type") val activityType: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null,

    ) : ActivityDataResponse(title, description, repeatEvery, cardColor, button) {

    suspend fun toTaskActivity(document: Document, taskId: String): TaskActivity =
        TaskActivity(
            taskId,
            id,
            title,
            description,
            button,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            image?.decodeBase64ImageFx()?.orNull(),
            activityType?.let { TaskActivityType.fromType(it) },
            welcomePage?.get(document)?.toPage(document)!!,
            pages?.get(document)?.mapNotNull { it.toPage(document) } ?: emptyList(),
            successPage?.get(document)?.toPage(document)
        )
}