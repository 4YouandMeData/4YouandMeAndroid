package com.foryouandme.entity.task

import com.foryouandme.entity.activity.StudyActivity
import org.threeten.bp.ZonedDateTime

data class Task(
    val id: String,
    val from: ZonedDateTime,
    val to: ZonedDateTime,
    val activity: StudyActivity
)