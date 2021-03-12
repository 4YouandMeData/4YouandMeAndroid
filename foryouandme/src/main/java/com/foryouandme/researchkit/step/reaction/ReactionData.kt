package com.foryouandme.researchkit.step.reaction

data class ReactionTimeState(
    val attempt: Int = 1,
    val reactionState: EReactionState = EReactionState.IDLE
)

enum class EReactionState {

    IDLE,
    REACTION,
    ERROR,

}

sealed class ReactionTimeStateUpdate {

    object Spawn : ReactionTimeStateUpdate()
    object Attempt : ReactionTimeStateUpdate()

}

sealed class ReactionTimeError {

    object AttemptError : ReactionTimeError()

}

sealed class ReactionTimeStateEvent {

    data class StartAttempt(
        val maximumStimulusIntervalSeconds: Long,
        val minimumStimulusIntervalSeconds: Long,
        val timeoutSeconds: Long
    ) : ReactionTimeStateEvent()

    object StartShakeTracking: ReactionTimeStateEvent()

    object StopShakeTracking: ReactionTimeStateEvent()

}