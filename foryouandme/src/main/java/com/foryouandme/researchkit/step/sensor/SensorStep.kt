package com.foryouandme.researchkit.step.sensor

import android.content.Context
import com.foryouandme.researchkit.recorder.RecorderConfig
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Step
import java.util.*

/**
 * @property target
 * The duration of the step, it can be represented by a specific amount of seconds or steps
 *
 * @property recorderConfigurations
 * An array of recorder configurations that define the parameters for recorders to be
 * run during a step to collect sensor or other data.
 * If you want to collect data from sensors while the step is in progress,
 * add one or more recorder configurations to the array.
 *
 * @property spokenInstruction
 * Localized text that represents an instructional voice prompt.
 * Instructional speech begins when the step starts.
 *
 * @property finishedSpokenInstruction
 * Localized text that represents an instructional voice prompt for when the step finishes.
 * Instructional speech begins when the step finishes.
 *
 * @property spokenInstructionMap
 * A map of <"time_in_seconds_to_speak", "what_to_speak">
 *
 * @property shouldVibrateOnFinish
 * A Boolean value indicating whether to vibrate when the step finishes.
 *
 * @property shouldPlaySoundOnFinish
 * A Boolean value indicating whether to play a default sound when the step finishes.
 *
 * @property estimateTimeInMsToSpeakEndInstruction
 * This can be increased to allow the ending spoken instruction to
 * not get cut off if it is too long
 *
 */
class SensorStep(
    identifier: String,
    block: Block,
    val backgroundColor: Int,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val image: Int?,
    val target: SensorRecorderTarget,
    val recorderConfigurations: List<RecorderConfig>,
    val timerSeconds: Long? = null,
    val spokenInstruction: ((Context) -> String)? = null,
    val finishedSpokenInstruction: String? = null,
    val spokenInstructionMap: Map<Long, String> = emptyMap(),
    val shouldVibrateOnFinish: Boolean = false,
    val shouldPlaySoundOnFinish: Boolean = false,
    val estimateTimeInMsToSpeakEndInstruction: Long = 1000

) : Step(
    identifier = identifier,
    back = null,
    block = block,
    skip = null,
    view = { SensorStepFragment() }
) {

    /**
     * The recording UUID is a unique identifier used by the RecorderService
     */
    val recordingUUID: UUID = UUID.randomUUID()

    fun hasVoice(): Boolean =
        spokenInstruction != null ||
                finishedSpokenInstruction != null ||
                spokenInstructionMap.isNotEmpty()

}

sealed class SensorRecorderTarget {

    data class Time(val duration: Int) : SensorRecorderTarget()

    data class Steps(val steps: Int) : SensorRecorderTarget()

}