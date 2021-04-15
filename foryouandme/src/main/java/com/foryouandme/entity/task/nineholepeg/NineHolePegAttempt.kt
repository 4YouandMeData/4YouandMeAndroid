package com.foryouandme.entity.task.nineholepeg

data class NineHolePegAttempt(
    val id: Int,
    val step: NineHolePegSubStep,
    val errorCount: Int = 0,
    val peg: Int = 1,
    val totalPegs: Int = 9
)