package com.foryouandme.researchkit.step.reaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.shake.StartShakeTrackingUseCase
import com.foryouandme.domain.usecase.shake.StopShakeTrackingUseCase
import com.foryouandme.entity.sensor.ShakeSensitivity
import com.foryouandme.researchkit.step.reaction.EReactionState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ReactionTimeViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ReactionTimeStateUpdate>,
    private val errorFlow: ErrorFlow<ReactionTimeError>,
    private val startShakeTrackingUseCase: StartShakeTrackingUseCase,
    private val stopShakeTrackingUseCase: StopShakeTrackingUseCase
) : ViewModel() {

    /* --- state --- */

    var state = ReactionTimeState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val error = errorFlow.error

    /* --- reaction --- */

    private var reactionJob: Job? = null

    private suspend fun startReaction(
        maximumStimulusIntervalSeconds: Long,
        minimumStimulusIntervalSeconds: Long,
        timeoutSeconds: Long
    ) {

        val spawnDelaySeconds =
            Random.nextLong(minimumStimulusIntervalSeconds, maximumStimulusIntervalSeconds)

        state = state.copy(reactionState = IDLE)
        delay(TimeUnit.SECONDS.toMillis(spawnDelaySeconds))
        state = state.copy(reactionState = REACTION)
        stateUpdateFlow.update(ReactionTimeStateUpdate.Spawn)
        delay(TimeUnit.SECONDS.toMillis(timeoutSeconds))
        state = state.copy(reactionState = IDLE)
        throw ForYouAndMeException.Unknown

    }

    /* --- shake --- */

    private var shakeJob: Job? = null

    private suspend fun onShake() {

        when(state.reactionState) {
            IDLE -> {
                reactionJob?.cancel()
                state = state.copy(reactionState = ERROR)
                throw ForYouAndMeException.Unknown
            }
            REACTION -> {
                reactionJob?.cancel()
                state = state.copy(attempt = state.attempt + 1)
                stateUpdateFlow.update(ReactionTimeStateUpdate.Attempt)
            }
            ERROR -> {}
        }

    }

    /* --- state event --- */

    fun execute(stateEvent: ReactionTimeStateEvent) {
        when (stateEvent) {
            is ReactionTimeStateEvent.StartAttempt -> {
                reactionJob =
                    errorFlow.launchCatch(viewModelScope, ReactionTimeError.AttemptError)
                    {
                        startReaction(
                            stateEvent.maximumStimulusIntervalSeconds,
                            stateEvent.minimumStimulusIntervalSeconds,
                            stateEvent.timeoutSeconds
                        )
                    }
            }
            ReactionTimeStateEvent.StartShakeTracking -> {
                shakeJob?.cancel()
                shakeJob = viewModelScope.launchSafe {

                    startShakeTrackingUseCase(ShakeSensitivity.Medium)
                        .collect {
                            errorFlow.launchCatch(viewModelScope, ReactionTimeError.AttemptError)
                            { onShake() }
                        }

                }
            }
            ReactionTimeStateEvent.StopShakeTracking ->
                viewModelScope.launchSafe { stopShakeTrackingUseCase() }
        }
    }

}