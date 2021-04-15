package com.foryouandme.entity.task.nineholepeg

data class NineHolePegAttempt(
    val id: Int,
    val step: NineHolePegSubStep,
    val errorCount: Int = 0
)