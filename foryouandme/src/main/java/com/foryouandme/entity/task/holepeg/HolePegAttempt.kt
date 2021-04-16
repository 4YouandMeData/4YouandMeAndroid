package com.foryouandme.entity.task.holepeg

import org.threeten.bp.ZonedDateTime

data class HolePegAttempt(
    val id: Int,
    val step: HolePegSubStep,
    val errorCount: Int = 0,
    val pegs: List<Peg> = listOf(Peg()),
    val totalPegs: Int = 9,
    val startDate: ZonedDateTime = ZonedDateTime.now(),
    val endDate: ZonedDateTime = ZonedDateTime.now(),
)