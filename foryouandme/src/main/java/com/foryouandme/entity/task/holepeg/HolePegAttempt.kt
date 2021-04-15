package com.foryouandme.entity.task.holepeg

data class HolePegAttempt(
    val id: Int,
    val step: HolePegSubStep,
    val errorCount: Int = 0,
    val peg: List<Peg> = listOf(Peg()),
    val totalPegs: Int = 9,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
)