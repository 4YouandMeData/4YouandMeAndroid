package org.fouryouandme.researchkit.task.fitness

import com.squareup.moshi.Moshi
import org.fouryouandme.R
import org.fouryouandme.researchkit.recorder.config.AccelerometerRecorderConfig
import org.fouryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import org.fouryouandme.researchkit.recorder.config.PedometerRecorderConfig
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.countdown.CountDownStep
import org.fouryouandme.researchkit.step.end.EndStep
import org.fouryouandme.researchkit.step.introduction.IntroductionStep
import org.fouryouandme.researchkit.step.sensor.SensorRecorderTarget
import org.fouryouandme.researchkit.step.sensor.SensorStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers

class FitnessTask(
    id: String,
    startBackgroundColor: Int,
    startTitle: String?,
    startTitleColor: Int,
    startDescription: String?,
    startDescriptionColor: Int,
    startImage: Int,
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
    walkBackgroundColor: Int,
    walkTitle: String?,
    walkTitleColor: Int,
    walkDescription: String?,
    walkDescriptionColor: Int,
    walkImage: Int?,
    sitBackgroundColor: Int,
    sitTitle: String?,
    sitTitleColor: Int,
    sitDescription: String?,
    sitDescriptionColor: Int,
    sitImage: Int?,
    endBackgroundColor: Int,
    endTitle: String?,
    endTitleColor: Int,
    endDescription: String?,
    endDescriptionColor: Int,
    endButton: String?,
    endButtonColor: Int,
    endButtonTextColor: Int,
    endClose: Boolean = false,
    endCheckMarkBackgroundColor: Int,
    endCheckMarkColor: Int,
    private val moshi: Moshi
) : Task(TaskIdentifiers.FITNESS, id) {

    override val steps: List<Step> by lazy {

        listOf(
            IntroductionStep(
                identifier = FITNESS_START,
                backgroundColor = startBackgroundColor,
                title = { startTitle ?: it.getString(R.string.FITNESS_title) },
                titleColor = startTitleColor,
                description = {
                    startDescription ?: it.getString(
                        R.string.FITNESS_start,
                        "one minute"
                    )
                },
                descriptionColor = startDescriptionColor,
                image = startImage,
                button = { startButton ?: it.getString(R.string.TASK_next) },
                buttonColor = startButtonColor,
                buttonTextColor = startButtonTextColor,
            ),
            IntroductionStep(
                identifier = FITNESS_INTRO,
                backgroundColor = introBackgroundColor,
                title = { introTitle ?: it.getString(R.string.FITNESS_title) },
                titleColor = introTitleColor,
                description = {
                    introDescription ?: it.getString(
                        R.string.FITNESS_intro,
                        "one minute",
                        "ten seconds"
                    )
                },
                descriptionColor = introDescriptionColor,
                image = introImage,
                button = { introButton ?: it.getString(R.string.TASK_get_started) },
                buttonColor = introButtonColor,
                buttonTextColor = introButtonTextColor,
            ),
            CountDownStep(
                identifier = FITNESS_COUNT_DOWN,
                backgroundColor = countDownBackgroundColor,
                titleColor = countDownTitleColor,
                title = { countDownTitle ?: it.getString(R.string.FITNESS_title) },
                description = { countDownDescription ?: it.getString(R.string.TASK_countdown) },
                descriptionColor = countDownDescriptionColor,
                seconds = countDownSeconds,
                counterColor = countDownCounterColor,
                counterProgressColor = countDownCounterProgressColor
            ),
            SensorStep(
                identifier = FITNESS_WALK,
                backgroundColor = walkBackgroundColor,
                title = { walkTitle ?: it.getString(R.string.FITNESS_title) },
                titleColor = walkTitleColor,
                description = {
                    walkDescription ?: it.getString(
                        R.string.FITNESS_walk,
                        "one minute"
                    )
                },
                descriptionColor = walkDescriptionColor,
                image = walkImage,
                target = SensorRecorderTarget.Time(60),
                recorderConfigurations =
                listOf(
                    DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                    AccelerometerRecorderConfig(moshi, 10.toDouble()),
                    PedometerRecorderConfig(moshi)
                ),
                spokenInstruction = {
                    walkDescription ?: it.getString(
                        R.string.FITNESS_walk,
                        "one minute"
                    )
                }
            ),
            SensorStep(
                identifier = FITNESS_SIT,
                backgroundColor = sitBackgroundColor,
                title = { sitTitle ?: it.getString(R.string.FITNESS_title) },
                titleColor = sitTitleColor,
                description = {
                    sitDescription ?: it.getString(
                        R.string.FITNESS_sit,
                        "ten seconds"
                    )
                },
                descriptionColor = sitDescriptionColor,
                image = sitImage,
                target = SensorRecorderTarget.Time(10),
                recorderConfigurations =
                listOf(
                    DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                    AccelerometerRecorderConfig(moshi, 10.toDouble()),
                    PedometerRecorderConfig(moshi)
                ),
                spokenInstruction = {
                    sitDescription ?: it.getString(
                        R.string.FITNESS_sit,
                        "ten seconds"
                    )
                }
            ),
            EndStep(
                identifier = FITNESS_END,
                backgroundColor = endBackgroundColor,
                title = { endTitle ?: it.getString(R.string.FITNESS_end_title) },
                titleColor = endTitleColor,
                description = { endDescription ?: it.getString(R.string.FITNESS_end_description) },
                descriptionColor = endDescriptionColor,
                button = { endButton ?: it.getString(R.string.FITNESS_button) },
                buttonColor = endButtonColor,
                buttonTextColor = endButtonTextColor,
                close = endClose,
                checkMarkBackgroundColor = endCheckMarkBackgroundColor,
                checkMarkColor = endCheckMarkColor
            )
        )
    }

    companion object {

        const val FITNESS_START: String = "fitness_start"

        const val FITNESS_INTRO: String = "fitness_intro"

        const val FITNESS_COUNT_DOWN: String = "fitness_count_down"

        const val FITNESS_WALK: String = "fitness_walk"

        const val FITNESS_SIT: String = "fitness_sit"

        const val FITNESS_END: String = "fitness_end"
    }
}