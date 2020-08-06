package org.fouryouandme.core.data.api.task.response

import arrow.core.None
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.some
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.data.api.common.response.activity.ActivityDataResponse
import org.fouryouandme.core.data.api.common.response.activity.QuickActivityResponse
import org.fouryouandme.core.data.api.common.response.activity.TaskActivityResponse
import org.fouryouandme.core.entity.task.Task
import org.threeten.bp.ZonedDateTime

@JsonApi(type = "task")
data class TaskResponse(
    @field:Json(name = "from") val from: String? = null,
    @field:Json(name = "to") val to: String? = null,
    @field:Json(name = "schedulable") val activity: HasOne<ActivityDataResponse>? = null
) : Resource() {


    fun toTask(): Option<Task> =
        Option.fx {
            Task(
                id,
                !from.toOption().map { ZonedDateTime.parse(it) },
                !to.toOption().map { ZonedDateTime.parse(it) },
                !activity?.get(document).toOption().flatMap {
                    when (it) {
                        is QuickActivityResponse -> it.toQuickActivity().some()
                        is TaskActivityResponse -> it.toTaskActivity().some()
                        else -> None
                    }
                }
            )
        }
}

fun Array<TaskResponse>.toTaskItems(): List<Task> = mapNotNull { it.toTask().orNull() }