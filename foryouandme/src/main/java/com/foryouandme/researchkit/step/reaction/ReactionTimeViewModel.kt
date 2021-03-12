package com.foryouandme.researchkit.step.reaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToLong
import kotlin.random.Random

@HiltViewModel
class ReactionTimeViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ReactionTimeStateUpdate>,
    private val errorFlow: ErrorFlow<ReactionTimeError>
) : ViewModel() {

    /* --- state --- */

    var state = ReactionTimeState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val error = errorFlow.error

    /* --- reaction --- */

    private suspend fun startReaction(
        maximumStimulusIntervalSeconds: Double,
        minimumStimulusIntervalSeconds: Double,
        thresholdAcceleration: Double,
        numberOfAttempts: Int,
        timeoutSeconds: Double
    ) {

        val spawnDelaySeconds =
            Random.nextDouble(minimumStimulusIntervalSeconds, maximumStimulusIntervalSeconds)

        delay(TimeUnit.SECONDS.toMillis(spawnDelaySeconds.roundToLong()))
        stateUpdateFlow.update(ReactionTimeStateUpdate.Spawn)

    }

    /* --- state event --- */

    fun execute(stateEvent: ReactionTimeStateEvent) {
        when (stateEvent) {
            is ReactionTimeStateEvent.StartAttempt ->
                errorFlow.launchCatch(viewModelScope, ReactionTimeError.AttemptError)
                {
                    startReaction(
                        stateEvent.maximumStimulusIntervalSeconds,
                        stateEvent.minimumStimulusIntervalSeconds,
                        stateEvent.thresholdAcceleration,
                        stateEvent.numberOfAttempts,
                        stateEvent.timeoutSeconds
                    )
                }

        }
    }

}