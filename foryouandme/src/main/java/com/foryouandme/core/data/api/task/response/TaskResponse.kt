package com.foryouandme.core.data.api.task.response

import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import com.foryouandme.core.data.api.common.response.activity.ActivityDataResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityResponse
import com.foryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import com.foryouandme.core.data.api.common.response.activity.TaskActivityResponse
import com.foryouandme.core.entity.task.Task
import com.foryouandme.core.ext.toEither
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.threeten.bp.ZonedDateTime

@JsonApi(type = "task")
data class TaskResponse(
    @field:Json(name = "from") val from: String? = null,
    @field:Json(name = "to") val to: String? = null,
    @field:Json(name = "rescheduled_times") val rescheduledTimes: Int? = null,
    @field:Json(name = "schedulable") val activity: HasOne<ActivityDataResponse>? = null
) : Resource() {


    suspend fun toTask(): Task? =
        either.invoke<Unit, Task> {

            Task(
                id,
                !from.toEither().map { ZonedDateTime.parse(it) },
                !to.toEither().map { ZonedDateTime.parse(it) },
                !activity?.get(document).toEither().flatMap {
                    when (it) {
                        is QuickActivityResponse -> it.toQuickActivity(id).toEither()
                        is TaskActivityResponse ->
                            it.toTaskActivity(document, id, rescheduledTimes).toEither()
                        is SurveyActivityResponse ->
                            it.toTaskActivity(document, id, rescheduledTimes).toEither()
                        else -> Unit.left()
                    }
                }
            )

        }.orNull()
}

suspend fun Array<TaskResponse>.toTaskItems(): List<Task> = mapNotNull { it.toTask() }

