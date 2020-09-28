package org.fouryouandme.researchkit.task.gait

import com.squareup.moshi.Moshi
import org.fouryouandme.R
import org.fouryouandme.researchkit.recorder.config.AccelerometerRecorderConfig
import org.fouryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import org.fouryouandme.researchkit.recorder.config.PedometerRecorderConfig
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.countdown.CountDownStep
import org.fouryouandme.researchkit.step.introduction.IntroductionStep
import org.fouryouandme.researchkit.step.sensor.SensorRecorderTarget
import org.fouryouandme.researchkit.step.sensor.SensorStep
import org.fouryouandme.researchkit.step.start.StartStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers

class GaitTask(
    id: String,
    startBackgroundColor: Int,
    startTitle: String?,
    startTitleColor: Int,
    startDescription: String?,
    startDescriptionColor: Int,
    startButton: String?,
    startButtonColor: Int,
    startButtonTextColor: Int,
    introBackgroundColor: Int,
    introTitle: String?,
    introTitleColor: Int,
    introDescription: String?,
    introDescriptionColor: Int,
    introImage: Int,
    introButton: String?,
    introButtonColor: Int,
    introButtonTextColor: Int,
    countDownBackgroundColor: Int,
    countDownTitle: String?,
    countDownTitleColor: Int,
    countDownDescription: String?,
    countDownDescriptionColor: Int,
    countDownSeconds: Int,
    countDownCounterColor: Int,
    countDownCounterProgressColor: Int,
    outboundBackgroundColor: Int,
    outboundTitle: String?,
    outboundTitleColor: Int,
    outboundDescription: String?,
    outboundDescriptionColor: Int,
    private val moshi: Moshi
) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        listOf(
            StartStep(
                identifier = GAIT_START,
                backgroundColor = startBackgroundColor,
                title = { startTitle ?: it.getString(R.string.GAIT_title) },
                titleColor = startTitleColor,
                description = { startDescription ?: it.getString(R.string.GAIT_start) },
                descriptionColor = startDescriptionColor,
                button = { startButton ?: it.getString(R.string.TASK_next) },
                buttonColor = startButtonColor,
                buttonTextColor = startButtonTextColor,
            ),
            IntroductionStep(
                identifier = GAIT_INTRO,
                backgroundColor = introBackgroundColor,
                title = { introTitle ?: it.getString(R.string.GAIT_title) },
                titleColor = introTitleColor,
                description = { introDescription ?: it.getString(R.string.GAIT_intro) },
                descriptionColor = introDescriptionColor,
                image = introImage,
                button = { introButton ?: it.getString(R.string.TASK_next) },
                buttonColor = introButtonColor,
                buttonTextColor = introButtonTextColor
            ),
            CountDownStep(
                identifier = GAIT_COUNT_DOWN,
                backgroundColor = countDownBackgroundColor,
                titleColor = countDownTitleColor,
                title = { countDownTitle ?: it.getString(R.string.GAIT_title) },
                description = { countDownDescription ?: it.getString(R.string.TASK_countdown) },
                descriptionColor = countDownDescriptionColor,
                seconds = countDownSeconds,
                counterColor = countDownCounterColor,
                counterProgressColor = countDownCounterProgressColor
            ),
            SensorStep(
                identifier = GAIT_OUTBOUND,
                backgroundColor = outboundBackgroundColor,
                title = { outboundTitle ?: it.getString(R.string.GAIT_title) },
                titleColor = outboundTitleColor,
                description = { outboundDescription ?: it.getString(R.string.GAIT_outbound, 20) },
                descriptionColor = outboundDescriptionColor,
                target = SensorRecorderTarget.Steps(20),
                recorderConfigurations =
                listOf(
                    DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                    AccelerometerRecorderConfig(moshi, 10.toDouble()),
                    PedometerRecorderConfig(moshi)
                ),
                spokenInstruction = {
                    outboundDescription ?: it.getString(
                        R.string.GAIT_outbound,
                        20
                    )
                }
            )
        )

    }

    companion object {

        const val GAIT_START: String = "gait_start"

        const val GAIT_INTRO: String = "gait_intro"

        const val GAIT_COUNT_DOWN: String = "gait_count_down"

        const val GAIT_OUTBOUND: String = "gait_outbound"

        const val GAIT_RETURN: String = "gait_return"

        const val GAIT_REST: String = "gait_rest"

    }

}