package com.foryouandme.data.repository.auth.answer.network.response.activity

import com.foryouandme.core.ext.decodeBase64Image
import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.activity.TaskActivityType
import com.foryouandme.entity.configuration.HEXGradient
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
    rescheduleIn: Int? = null,
    rescheduleTimes: Int? = null,
    button: String? = null,
    @field:Json(name = "activity_type") val activityType: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null,
) : ActivityDataResponse(
    title,
    description,
    repeatEvery,
    cardColor,
    rescheduleIn,
    rescheduleTimes,
    button
) {

    fun toTaskActivity(
        document: Document,
        taskId: String,
        rescheduledTimes: Int?
    ): TaskActivity? {

        val welcomePage = welcomePage?.get(document)?.toPage(document)

        return when (null) {
            welcomePage -> null
            else ->
                TaskActivity(
                    taskId,
                    id,
                    title,
                    description,
                    button,
                    if (cardColor != null) HEXGradient(cardColor, cardColor) else null,
                    image,
                    activityType?.let { TaskActivityType.fromType(it) },
                    welcomePage,
                    pages?.get(document)?.mapNotNull { it.toPage(document) } ?: emptyList(),
                    successPage?.get(document)?.toPage(document),
                    if (rescheduleIn != null && rescheduleTimes != null)
                        Reschedule(rescheduleIn, rescheduleTimes, rescheduledTimes)
                    else null
                )
        }
    }

}