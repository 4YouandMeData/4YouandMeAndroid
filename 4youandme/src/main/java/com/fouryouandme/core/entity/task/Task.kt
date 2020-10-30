package com.fouryouandme.core.entity.task

import com.fouryouandme.core.entity.activity.StudyActivity
import org.threeten.bp.ZonedDateTime

data class Task(
    val id: String,
    val from: ZonedDateTime,
    val to: ZonedDateTime,
    val activity: StudyActivity
)