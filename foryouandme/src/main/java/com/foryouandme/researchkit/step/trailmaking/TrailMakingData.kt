package com.foryouandme.researchkit.step.trailmaking

data class TrailMakingState(
    val points: List<TrailMakingPoint> = emptyList(),
    val currentIndex: Int = 0,
    val secondsElapsed: Long = 0,
    val errorCount: Int = 0
)

sealed class TrailMakingStateUpdate {

    object Initialized : TrailMakingStateUpdate()
    object CurrentIndex : TrailMakingStateUpdate()
    object SecondsElapsed : TrailMakingStateUpdate()
    object ErrorCount: TrailMakingStateUpdate()

}

sealed class TrailMakingError {

    data class WrongPoint(val point: TrailMakingPoint) : TrailMakingError()

}

sealed class TrailMakingStateEvent {

    data class Initialize(val type: ETrailMakingType) : TrailMakingStateEvent()
    data class SelectPoint(val point: TrailMakingPoint) : TrailMakingStateEvent()
    object StartTimer : TrailMakingStateEvent()

}

data class Point(val x: Double, val y: Double)
data class TrailMakingPoint(val x: Double, val y: Double, val name: String)



