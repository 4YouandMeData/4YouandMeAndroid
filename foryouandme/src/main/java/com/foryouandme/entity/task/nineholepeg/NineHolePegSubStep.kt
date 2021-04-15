package com.foryouandme.entity.task.nineholepeg

data class NineHolePegSubStep(
    val start: NineHolePegPointPosition,
    val target: NineHolePegTargetPosition,
)

fun List<NineHolePegSubStep>.toNineHolePegAttempts(): List<NineHolePegAttempt> =
    mapIndexed { index, step -> NineHolePegAttempt(index, step, 0) }