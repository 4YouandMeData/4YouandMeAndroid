package com.foryouandme.entity.task.holepeg

import org.threeten.bp.ZonedDateTime

data class Peg(
    val startDate: ZonedDateTime = ZonedDateTime.now(),
    val endDate: ZonedDateTime = ZonedDateTime.now(),
    val distance: Double = 0.0
)