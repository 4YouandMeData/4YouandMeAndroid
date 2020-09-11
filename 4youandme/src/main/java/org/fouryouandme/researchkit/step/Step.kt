package org.fouryouandme.researchkit.step

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.recorder.RecorderConfig
import java.util.*

sealed class Step(val identifier: String, val configuration: Configuration) {

    class IntroductionStep(
        identifier: String,
        configuration: Configuration,
        val title: String,
        val description: String,
        val image: Int,
        val button: String,
        val close: Boolean = false
    ) : Step(identifier, configuration)

    class CountDownStep(
        identifier: String,
        configuration: Configuration,
        val title: String,
        val seconds: Int
    ) : Step(identifier, configuration)


    /**
     * @property duration
     * The duration of the step in seconds.
     * If the step duration is greater than zero, a built-in timer starts when the
     * step starts. If `shouldStartTimerAutomatically` is set, the timer
     * starts when the step's view appears. When the timer expires, a sound or
     * vibration may be played. If `shouldContinueOnFinish` is set, the step
     * automatically navigates forward when the timer expires.
     * The default value of this property is `0`, which disables the built-in timer.
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
    class ActiveStep(
        identifier: String,
        configuration: Configuration,
        val title: String,
        val description: String,
        val duration: Int = 0,
        val recorderConfigurations: List<RecorderConfig>,
        val spokenInstruction: Option<String> = None,
        val finishedSpokenInstruction: Option<String> = None,
        val spokenInstructionMap: Map<Long, String> = emptyMap(),
        val shouldVibrateOnFinish: Boolean = false,
        val shouldPlaySoundOnFinish: Boolean = false,
        val estimateTimeInMsToSpeakEndInstruction: Long = 1000

    ) : Step(identifier, configuration) {

        /**
         * The recording UUID is a unique identifier used by the RecorderService
         */
        val recordingUUID: UUID = UUID.randomUUID()

        fun hasVoice(): Boolean =
            spokenInstruction.isDefined() ||
                    finishedSpokenInstruction.isDefined() ||
                    spokenInstructionMap.isNotEmpty()

    }

    class StartStep(
        identifier: String,
        configuration: Configuration,
        val title: String,
        val description: String,
        val button: String,
        val close: Boolean = false
    ) : Step(identifier, configuration)

}