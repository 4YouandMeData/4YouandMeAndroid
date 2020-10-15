package org.fouryouandme.core.data.api.task.response

import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.data.api.common.response.activity.ActivityDataResponse
import org.fouryouandme.core.data.api.common.response.activity.QuickActivityResponse
import org.fouryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import org.fouryouandme.core.data.api.common.response.activity.TaskActivityResponse
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.toEither
import org.threeten.bp.ZonedDateTime

@JsonApi(type = "task")
data class TaskResponse(
    @field:Json(name = "from") val from: String? = null,
    @field:Json(name = "to") val to: String? = null,
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
                        is TaskActivityResponse -> it.toTaskActivity(id).toEither()
                        is SurveyActivityResponse -> it.toTaskActivity(id).toEither()
                        else -> Unit.left()
                    }
                }
            )

        }.orNull()
}

suspend fun Array<TaskResponse>.toTaskItems(): List<Task> = mapNotNull { it.toTask() }

