package com.foryouandme.entity.task

import com.foryouandme.entity.activity.StudyActivity
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.mock.Mock
import org.threeten.bp.ZonedDateTime

data class Task(
    val id: String,
    val from: ZonedDateTime,
    val to: ZonedDateTime,
    val activity: StudyActivity
) {

    companion object {

        fun mock(): Task =
            Task(
                id = "id",
                from = Mock.zoneDateTime,
                to = Mock.zoneDateTime,
                activity = TaskActivity.mock()
            )

    }

}