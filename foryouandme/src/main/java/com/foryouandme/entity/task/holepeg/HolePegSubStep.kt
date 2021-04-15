package com.foryouandme.entity.task.holepeg

data class HolePegSubStep(
    val start: HolePegPointPosition,
    val target: HolePegTargetPosition,
)

fun List<HolePegSubStep>.toHolePegAttempts(): List<HolePegAttempt> =
    mapIndexed { index, step -> HolePegAttempt(index, step, 0) }