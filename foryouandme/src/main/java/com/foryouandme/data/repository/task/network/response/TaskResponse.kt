package com.foryouandme.data.repository.task.network.response

import com.foryouandme.core.data.api.common.response.activity.ActivityDataResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityResponse
import com.foryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import com.foryouandme.core.data.api.common.response.activity.TaskActivityResponse
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.entity.task.Task
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


    fun toTask(): Task? {

        val from = catchToNull { from?.let { ZonedDateTime.parse(it) } }
        val to = catchToNull { to?.let { ZonedDateTime.parse(it) } }

        val activity =
            activity?.get(document)?.let {
                when (it) {
                    is QuickActivityResponse -> it.toQuickActivity(id)
                    is TaskActivityResponse ->
                        it.toTaskActivity(document, id, rescheduledTimes)
                    is SurveyActivityResponse ->
                        it.toTaskActivity(document, id, rescheduledTimes)
                    else -> null
                }
            }

        return when (null) {
            from, to, activity -> null
            else ->
                Task(
                    id,
                    from,
                    to,
                    activity
                )
        }

    }
}

fun Array<TaskResponse>.toTaskItems(): List<Task> = mapNotNull { it.toTask() }

