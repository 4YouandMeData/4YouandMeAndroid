package com.foryouandme.entity.task.holepeg

data class HolePegAttempt(
    val id: Int,
    val step: HolePegSubStep,
    val errorCount: Int = 0,
    val peg: Int = 1,
    val totalPegs: Int = 9
)