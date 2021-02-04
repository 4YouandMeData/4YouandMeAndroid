package com.foryouandme.researchkit.task.fitness

import com.foryouandme.R
import com.foryouandme.researchkit.recorder.config.AccelerometerRecorderConfig
import com.foryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import com.foryouandme.researchkit.recorder.config.PedometerRecorderConfig
import com.foryouandme.researchkit.recorder.sensor.accelerometer.AccelerometerRecorder
import com.foryouandme.researchkit.recorder.sensor.motion.DeviceMotionRecorder
import com.foryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.beforestart.WelcomeStep
import com.foryouandme.researchkit.step.countdown.CountDownStep
import com.foryouandme.researchkit.step.end.EndStep
import com.foryouandme.researchkit.step.introduction.IntroductionStep
import com.foryouandme.researchkit.step.sensor.SensorRecorderTarget
import com.foryouandme.researchkit.step.sensor.SensorStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.squareup.moshi.Moshi

class FitnessTask(
    id: String,
    welcomeRemindMeLater: Boolean,
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
    startImage: Int,
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

            WelcomeStep(
                identifier = FITNESS_WELCOME,
                back = Back(welcomeBackImage),
                backgroundColor = welcomeBackgroundColor,
                image = welcomeImage,
                title = { welcomeTitle ?: it.getString(R.string.FITNESS_welcome_title) },
                titleColor = welcomeTitleColor,
                description = {
                    welcomeDescription ?: it.getString(R.string.FITNESS_welcome_description)
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
                shadowColor = welcomeShadowColor,
                remindMeLater = welcomeRemindMeLater
            )

        ).plus(

            getFitnessCoreSteps(
                startBackImage = startBackImage,
                startBackgroundColor = startBackgroundColor,
                startTitle = startTitle,
                startTitleColor = startTitleColor,
                startDescription = startDescription,
                startDescriptionColor = startDescriptionColor,
                startImage = startImage,
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
                walkBackgroundColor = walkBackgroundColor,
                walkTitle = walkTitle,
                walkTitleColor = walkTitleColor,
                walkDescription = walkDescription,
                walkDescriptionColor = walkDescriptionColor,
                walkImage = walkImage,
                sitBackgroundColor = sitBackgroundColor,
                sitTitle = sitTitle,
                sitTitleColor = sitTitleColor,
                sitDescription = sitDescription,
                sitDescriptionColor = sitDescriptionColor,
                sitImage = sitImage,
                moshi
            )


        ).plus(

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

        const val FITNESS_WELCOME: String = "fitness_welcome"

        const val FITNESS_START: String = "fitness_start"

        const val FITNESS_INTRO: String = "fitness_intro"

        const val FITNESS_COUNT_DOWN: String = "fitness_count_down"

        const val FITNESS_WALK: String = "fitness_walk"

        const val FITNESS_SIT: String = "fitness_sit"

        const val FITNESS_END: String = "fitness_end"


        fun getFitnessCoreSteps(
            startBackImage: Int,
            startBackgroundColor: Int,
            startTitle: String?,
            startTitleColor: Int,
            startDescription: String?,
            startDescriptionColor: Int,
            startImage: Int,
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
            moshi: Moshi
        ): List<Step> =
            listOf(
                IntroductionStep(
                    identifier = FITNESS_START,
                    back = Back(startBackImage),
                    backgroundColor = startBackgroundColor,
                    title = { startTitle ?: it.getString(R.string.FITNESS_title) },
                    titleColor = startTitleColor,
                    description = {
                        startDescription ?: it.getString(
                            R.string.FITNESS_start,
                            "two minute"
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
                    back = Back(introBackImage),
                    backgroundColor = introBackgroundColor,
                    title = { introTitle ?: it.getString(R.string.FITNESS_title) },
                    titleColor = introTitleColor,
                    description = {
                        introDescription ?: it.getString(
                            R.string.FITNESS_intro,
                            "two minute",
                            "thirty seconds"
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
                    back = Back(countDownBackImage),
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
                            "two minute"
                        )
                    },
                    descriptionColor = walkDescriptionColor,
                    image = walkImage,
                    target = SensorRecorderTarget.Time(120),
                    recorderConfigurations =
                    listOf(
                        DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                        AccelerometerRecorderConfig(moshi, 10.toDouble()),
                        PedometerRecorderConfig(moshi)
                    ),
                    spokenInstruction = {
                        walkDescription ?: it.getString(
                            R.string.FITNESS_walk,
                            "two minute"
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
                            "thirty seconds"
                        )
                    },
                    descriptionColor = sitDescriptionColor,
                    image = sitImage,
                    target = SensorRecorderTarget.Time(30),
                    recorderConfigurations =
                    listOf(
                        DeviceMotionRecorderConfig(moshi, 10.toDouble()),
                        AccelerometerRecorderConfig(moshi, 10.toDouble()),
                    ),
                    spokenInstruction = {
                        sitDescription ?: it.getString(
                            R.string.FITNESS_sit,
                            "thirty seconds"
                        )
                    }
                )
            )


        /* --- result keys --- */

        const val FITNESS_WALK_PEDOMETER: String =
            "${PedometerRecorder.PEDOMETER_IDENTIFIER}_${FITNESS_WALK}"

        const val FITNESS_WALK_DEVICE_MOTION: String =
            "${DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER}_${FITNESS_WALK}"

        const val FITNESS_WALK_ACCELEROMETER: String =
            "${AccelerometerRecorder.ACCELEROMETER_IDENTIFIER}_${FITNESS_WALK}"


        const val FITNESS_SIT_DEVICE_MOTION: String =
            "${DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER}_${FITNESS_SIT}"

        const val FITNESS_SIT_ACCELEROMETER: String =
            "${AccelerometerRecorder.ACCELEROMETER_IDENTIFIER}_${FITNESS_SIT}"

    }
}