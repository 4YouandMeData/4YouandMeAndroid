package com.foryouandme.researchkit.step.reaction

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.shake.StartShakeTrackingUseCase
import com.foryouandme.domain.usecase.shake.StopShakeTrackingUseCase
import com.foryouandme.entity.sensor.ShakeSensitivity
import com.foryouandme.researchkit.recorder.Recorder
import com.foryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.step.reaction.EReactionState.*
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ReactionTimeViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ReactionTimeStateUpdate>,
    private val errorFlow: ErrorFlow<ReactionTimeError>,
    private val startShakeTrackingUseCase: StartShakeTrackingUseCase,
    private val stopShakeTrackingUseCase: StopShakeTrackingUseCase,
    private val moshi: Moshi,
    @ApplicationContext private val application: Context
) : ViewModel() {

    /* --- state --- */

    var state = ReactionTimeState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val error = errorFlow.error

    /* --- reaction --- */

    private var reactionJob: Job? = null

    // TODO: refactor recorder with use case
    private var recorder: Recorder? = null

    private suspend fun startReaction(
        maximumStimulusIntervalSeconds: Long,
        minimumStimulusIntervalSeconds: Long,
        timeoutSeconds: Long,
        step: ReactionTimeStep,
        outputDirectory: File
    ) {

        val spawnDelaySeconds =
            Random.nextLong(minimumStimulusIntervalSeconds, maximumStimulusIntervalSeconds)

        state = state.copy(reactionState = IDLE)
        delay(TimeUnit.SECONDS.toMillis(spawnDelaySeconds))
        state = state.copy(reactionState = REACTION)
        recorder =
            DeviceMotionRecorderConfig(moshi, 10.toDouble())
                .recorderForStep(step, outputDirectory)
        recorder?.start(application)
        stateUpdateFlow.update(ReactionTimeStateUpdate.Spawn)
        delay(TimeUnit.SECONDS.toMillis(timeoutSeconds))
        state = state.copy(reactionState = IDLE)
        recorder?.cancel()
        recorder = null
        throw ForYouAndMeException.Unknown

    }

    /* --- shake --- */

    private var shakeJob: Job? = null

    private suspend fun onShake() {

        when (state.reactionState) {
            IDLE -> {
                reactionJob?.cancel()
                state = state.copy(reactionState = ERROR)
                recorder?.cancel()
                recorder = null
                throw ForYouAndMeException.Unknown
            }
            REACTION -> {
                reactionJob?.cancel()
                state = state.copy(attempt = state.attempt + 1)
                stateUpdateFlow.update(ReactionTimeStateUpdate.Attempt)
                recorder?.stop()?.let {

                    //update the file result id to handle multiple result for this step
                    val updatedResult =
                        FileResult(
                            identifier = "${it.identifier}_${state.results.size + 1}",
                            it.file,
                            it.contentType,
                            it.startDate,
                            it.endDate
                        )
                    state = state.copy(results = state.results.plus(updatedResult))

                }
                recorder = null
            }
            ERROR -> {
            }
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
                            stateEvent.timeoutSeconds,
                            stateEvent.step,
                            stateEvent.outputDirectory
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