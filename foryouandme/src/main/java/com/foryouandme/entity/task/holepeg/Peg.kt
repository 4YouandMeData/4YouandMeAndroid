package com.foryouandme.entity.task.holepeg

data class Peg(
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val distance: Double = 0.0
)