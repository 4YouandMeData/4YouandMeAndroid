package org.fouryouandme.core.entity.task

import org.fouryouandme.core.entity.configuration.HEXGradient
import org.threeten.bp.ZonedDateTime

data class Task(
    val id: String,
    val image: String,
    val title: String,
    val body: String,
    val button: String,
    val background: HEXGradient,
    val date: ZonedDateTime
)