package org.fouryouandme.researchkit.task.gait

import com.squareup.moshi.Moshi
import org.fouryouandme.R
import org.fouryouandme.researchkit.recorder.config.AccelerometerRecorderConfig
import org.fouryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import org.fouryouandme.researchkit.recorder.config.PedometerRecorderConfig
import org.fouryouandme.researchkit.recorder.sensor.accelerometer.AccelerometerRecorder
import org.fouryouandme.researchkit.recorder.sensor.motion.DeviceMotionRecorder
import org.fouryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import org.fouryouandme.researchkit.step.Back
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.beforestart.WelcomeStep
import org.fouryouandme.researchkit.step.countdown.CountDownStep
import org.fouryouandme.researchkit.step.end.EndStep
import org.fouryouandme.researchkit.step.introduction.IntroductionStep
import org.fouryouandme.researchkit.step.sensor.SensorRecorderTarget
import org.fouryouandme.researchkit.step.sensor.SensorStep
import org.fouryouandme.researchkit.step.start.StartStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers

class GaitTask(
    id: String,
    welcomeBackImage: Int,
    welcomeBackgroundColor: Int,
    welcomeImage: Int,
    welcomeTitle: String?,
    welcomeTitleColor: Int,
    welcomeDescription: String?,
    welcomeDescriptionColor: Int,
    welcomeRemindButton: String?,
    welcomeRemindButtonColor: Int,
    welcomeRemindButtonTextColor: Int,
    welcomeStartButton: String?,
    welcomeStartButtonColor: Int,
    welcomeStartButtonTextColor: Int,
    welcomeShadowColor: Int,
    startBackImage: Int,
    startBackgroundColor: Int,
    startTitle: String?,
    startTitleColor: Int,
    startDescription: String?,
    startDescriptionColor: Int,
    startButton: String?,
    startButtonColor: Int,
    startButtonTextColor: Int,
    introBackImage: Int,
    introBackgroundColor: Int,
    introTitle: String?,
    introTitleColor: Int,
    introDescription: String?,
    introDescriptionColor: Int,
    introImage: Int,
    introButton: String?,
    introButtonColor: Int,
    introButtonTextColor: Int,
    countDownBackImage: Int,
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
    returnBackgroundColor: Int,
    returnTitle: String?,
    returnTitleColor: Int,
    returnDescription: String?,
    returnDescriptionColor: Int,
    restBackgroundColor: Int,
    restTitle: String?,
    restTitleColor: Int,
    restDescription: String?,
    restDescriptionColor: Int,
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
) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        listOf(
            WelcomeStep(
                identifier = GAIT_WELCOME,
                back = Back(welcomeBackImage),
                backgroundColor = welcomeBackgroundColor,
                image = welcomeImage,
                title = { welcomeTitle ?: it.getString(R.string.GAIT_welcome_title) },
                titleColor = welcomeTitleColor,
                description = {
                    welcomeDescription ?: it.getString(R.string.GAIT_welcome_description)
                },
                descriptionColor = welcomeDescriptionColor,
                remindButton = {
                    welcomeRemindButton ?: it.getString(R.string.TASK_welcome_remind_button)
                },
                remindButtonColor = welcomeRemindButtonColor,
                remindButtonTextColor = welcomeRemindButtonTextColor,
                startButton = {
                    welcomeStartButton ?: it.getString(R.string.TASK_welcome_start_button)
                },
                startButtonColor = welcomeStartButtonColor,
                startButtonTextColor = welcomeStartButtonTextColor,
                shadowColor = welcomeShadowColor
            )
        ).plus(
            getGaitCoreSteps(
                startBackImage = startBackImage,
                startBackgroundColor = startBackgroundColor,
                startTitle = startTitle,
                startTitleColor = startTitleColor,
                startDescription = startDescription,
                startDescriptionColor = startDescriptionColor,
                startButton = startButton,
                startButtonColor = startButtonColor,
                startButtonTextColor = startButtonTextColor,
                introBackImage = introBackImage,
                introBackgroundColor = introBackgroundColor,
                introTitle = introTitle,
                introTitleColor = introTitleColor,
                introDescription = introDescription,
                introDescriptionColor = introDescriptionColor,
                introImage = introImage,
                introButton = introButton,
                introButtonColor = introButtonColor,
                introButtonTextColor = introButtonTextColor,
                countDownBackImage = countDownBackImage,
                countDownBackgroundColor = countDownBackgroundColor,
                countDownTitle = countDownTitle,
                countDownTitleColor = countDownTitleColor,
                countDownDescription = countDownDescription,
                countDownDescriptionColor = countDownDescriptionColor,
                countDownSeconds = countDownSeconds,
                countDownCounterColor = countDownCounterColor,
                countDownCounterProgressColor = countDownCounterProgressColor,
                outboundBackgroundColor = outboundBackgroundColor,
                outboundTitle = outboundTitle,
                outboundTitleColor = outboundTitleColor,
                outboundDescription = outboundDescription,
                outboundDescriptionColor = outboundDescriptionColor,
                returnBackgroundColor = returnBackgroundColor,
                returnTitle = returnTitle,
                returnTitleColor = returnTitleColor,
                returnDescription = returnDescription,
                returnDescriptionColor = returnDescriptionColor,
                restBackgroundColor = restBackgroundColor,
                restTitle = restTitle,
                restTitleColor = restTitleColor,
                restDescription = restDescription,
                restDescriptionColor = restDescriptionColor,
                moshi = moshi
            )
        ).plus(
            EndStep(
                identifier = GAIT_END,
                backgroundColor = endBackgroundColor,
                title = { endTitle ?: it.getString(R.string.GAIT_end_title) },
                titleColor = endTitleColor,
                description = { endDescription ?: it.getString(R.string.GAIT_end_description) },
                descriptionColor = endDescriptionColor,
                button = { endButton ?: it.getString(R.string.GAIT_button) },
                buttonColor = endButtonColor,
                buttonTextColor = endButtonTextColor,
                close = endClose,
                checkMarkBackgroundColor = endCheckMarkBackgroundColor,
                checkMarkColor = endCheckMarkColor
            )
        )

    }


    companion object {

        const val GAIT_WELCOME: String = "gait_welcome"

        const val GAIT_START: String = "gait_start"

        const val GAIT_INTRO: String = "gait_intro"

        const val GAIT_COUNT_DOWN: String = "gait_count_down"

        const val GAIT_OUTBOUND: String = "gait_outbound"

        const val GAIT_RETURN: String = "gait_return"

        const val GAIT_REST: String = "gait_rest"

        const val GAIT_END: String = "gait_end"


        /* --- result keys --- */

        const val GAIT_OUTBOUND_PEDOMETER: String =
            "${PedometerRecorder.PEDOMETER_IDENTIFIER}_${GAIT_OUTBOUND}"

        const val GAIT_OUTBOUND_DEVICE_MOTION: String =
            "${DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER}_${GAIT_OUTBOUND}"

        const val GAIT_OUTBOUND_ACCELEROMETER: String =
            "${AccelerometerRecorder.ACCELEROMETER_IDENTIFIER}_${GAIT_OUTBOUND}"


        const val GAIT_RETURN_PEDOMETER: String =
            "${PedometerRecorder.PEDOMETER_IDENTIFIER}_${GAIT_RETURN}"

        const val GAIT_RETURN_DEVICE_MOTION: String =
            "${DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER}_${GAIT_RETURN}"

        const val GAIT_RETURN_ACCELEROMETER: String =
            "${AccelerometerRecorder.ACCELEROMETER_IDENTIFIER}_${GAIT_RETURN}"


        const val GAIT_REST_DEVICE_MOTION: String =
            "${DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER}_${GAIT_REST}"

        const val GAIT_REST_ACCELEROMETER: String =
            "${AccelerometerRecorder.ACCELEROMETER_IDENTIFIER}_${GAIT_REST}"


        fun getGaitCoreSteps(
            startBackImage: Int,
            startBackgroundColor: Int,
            startTitle: String?,
            startTitleColor: Int,
            startDescription: String?,
            startDescriptionColor: Int,
            startButton: String?,
            startButtonColor: Int,
            startButtonTextColor: Int,
            introBackImage: Int,
            introBackgroundColor: Int,
            introTitle: String?,
            introTitleColor: Int,
            introDescription: String?,
            introDescriptionColor: Int,
            introImage: Int,
            introButton: String?,
            introButtonColor: Int,
            introButtonTextColor: Int,
            countDownBackImage: Int,
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
            returnBackgroundColor: Int,
            returnTitle: String?,
            returnTitleColor: Int,
            returnDescription: String?,
            returnDescriptionColor: Int,
            restBackgroundColor: Int,
            restTitle: String?,
            restTitleColor: Int,
            restDescription: String?,
            restDescriptionColor: Int,
            moshi: Moshi
        ): List<Step> =

            listOf(
                StartStep(
                    identifier = GAIT_START,
                    back = Back(startBackImage),
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
                    back = Back(introBackImage),
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
                    back = Back(countDownBackImage),
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
                    description = {
                        outboundDescription ?: it.getString(
                            R.string.GAIT_outbound,
                            20
                        )
                    },
                    descriptionColor = outboundDescriptionColor,
                    image = null,
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
                ),
                SensorStep(
                    identifier = GAIT_RETURN,
                    backgroundColor = returnBackgroundColor,
                    title = { returnTitle ?: it.getString(R.string.GAIT_title) },
                    titleColor = returnTitleColor,
                    description = { returnDescription ?: it.getString(R.string.GAIT_return) },
                    descriptionColor = returnDescriptionColor,
                    image = null,
                    target = SensorRecorderTarget.Steps(20),
                    recorderConfigurations =
                    listOf(
                        DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                        AccelerometerRecorderConfig(moshi, 10.toDouble()),
                        PedometerRecorderConfig(moshi)
                    ),
                    spokenInstruction = {
                        returnDescription ?: it.getString(
                            R.string.GAIT_return
                        )
                    }
                ),
                SensorStep(
                    identifier = GAIT_REST,
                    backgroundColor = restBackgroundColor,
                    title = { restTitle ?: it.getString(R.string.GAIT_title) },
                    titleColor = restTitleColor,
                    description = { restDescription ?: it.getString(R.string.GAIT_stand) },
                    descriptionColor = restDescriptionColor,
                    image = null,
                    target = SensorRecorderTarget.Time(20),
                    recorderConfigurations =
                    listOf(
                        DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                        AccelerometerRecorderConfig(moshi, 10.toDouble()),
                    ),
                    spokenInstruction = {
                        returnDescription ?: it.getString(
                            R.string.GAIT_stand
                        )
                    }
                )
            )

    }

}