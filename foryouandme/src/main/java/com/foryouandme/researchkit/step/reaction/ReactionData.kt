package com.foryouandme.researchkit.step.reaction

data class ReactionTimeState(
    val attempt: Int = 1
)

sealed class ReactionTimeStateUpdate {

    object Spawn : ReactionTimeStateUpdate()

}

sealed class ReactionTimeError {

    object AttemptError : ReactionTimeError()

}

sealed class ReactionTimeStateEvent {

    data class StartAttempt(
        val maximumStimulusIntervalSeconds: Double,
        val minimumStimulusIntervalSeconds: Double,
        val thresholdAcceleration: Double,
        val numberOfAttempts: Int,
        val timeoutSeconds: Double
    ) : ReactionTimeStateEvent()

    object StartShakeTracking: ReactionTimeStateEvent()

    object StopShakeTracking: ReactionTimeStateEvent()

}