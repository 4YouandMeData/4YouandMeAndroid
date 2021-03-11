package com.foryouandme.researchkit.step.trailmaking

import com.foryouandme.entity.task.trailmaking.TrailMakingPoint
import com.foryouandme.entity.task.trailmaking.TrailMakingTap

data class TrailMakingState(
    val points: List<TrailMakingPoint> = emptyList(),
    val currentIndex: Int = 0,
    val secondsElapsed: Long = 0,
    val errorCount: Int = 0,
    val taps: List<TrailMakingTap> = emptyList()
)

sealed class TrailMakingStateUpdate {

    object Initialized : TrailMakingStateUpdate()
    object CurrentIndex : TrailMakingStateUpdate()
    object SecondsElapsed : TrailMakingStateUpdate()
    object ErrorCount : TrailMakingStateUpdate()

}

sealed class TrailMakingError {

    data class WrongPoint(val point: TrailMakingPoint) : TrailMakingError()

}

sealed class TrailMakingStateEvent {

    data class Initialize(val type: ETrailMakingType) : TrailMakingStateEvent()
    data class SelectPoint(val point: TrailMakingPoint) : TrailMakingStateEvent()
    object StartTimer : TrailMakingStateEvent()

}


