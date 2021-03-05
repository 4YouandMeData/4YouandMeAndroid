package com.foryouandme.researchkit.step.trailmaking

data class TrailMakingState(
    val points: List<TrailMakingPoint> = emptyList()
)

sealed class TrailMakingStateUpdate {

    object Initialized : TrailMakingStateUpdate()

}

sealed class TrailMakingStateEvent {

    object Initialize : TrailMakingStateEvent()

}

data class TrailMakingPoint(val x: Double, val y: Double)

