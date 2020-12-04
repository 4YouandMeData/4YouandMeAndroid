package com.foryouandme.core.data.api.common.response.activity

import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.activity.TaskActivityType
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.core.ext.decodeBase64ImageFx
import com.foryouandme.core.ext.mapNotNull
import com.squareup.moshi.Json
import moe.banana.jsonapi2.Document
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi

@JsonApi(type = "survey")
class SurveyActivityResponse(
    title: String? = null,
    description: String? = null,
    cardColor: String? = null,
    rescheduleIn: Int? = null,
    rescheduleTimes: Int? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null,
) : ActivityDataResponse(
    title,
    description,
    null,
    cardColor,
    rescheduleIn,
    rescheduleTimes
) {

    suspend fun toTaskActivity(
        document: Document,
        taskId: String,
        rescheduledTimes: Int?
    ): TaskActivity =
            TaskActivity(
                taskId,
                id,
                title,
                description,
                null,
                mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
                image?.decodeBase64ImageFx()?.orNull(),
                TaskActivityType.Survey,
                welcomePage?.get(document)?.toPage(document)!!,
                pages?.get(document)?.mapNotNull { it.toPage(document) } ?: emptyList(),
                successPage?.get(document)?.toPage(document),
                mapNotNull(rescheduleIn, rescheduleTimes)
                    ?.let { Reschedule(it.a, it.b, rescheduledTimes) }
            )

}