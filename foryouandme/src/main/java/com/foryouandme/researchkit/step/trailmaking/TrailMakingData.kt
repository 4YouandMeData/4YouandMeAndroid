package com.foryouandme.researchkit.step.trailmaking

data class TrailMakingState(
    val points: List<TrailMakingPoint> = emptyList()
)

sealed class TrailMakingStateUpdate {

    object Initialized : TrailMakingStateUpdate()

}

sealed class TrailMakingStateEvent {

    data class Initialize(val type: ETrailMakingType) : TrailMakingStateEvent()

}

data class Point(val x: Double, val y: Double)
data class TrailMakingPoint(val x: Double, val y: Double, val name: String)



