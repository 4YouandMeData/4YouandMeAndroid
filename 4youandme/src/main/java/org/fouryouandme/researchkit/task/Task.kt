package org.fouryouandme.researchkit.task

import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.recorder.config.AccelerometerRecorderConfig
import org.fouryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import org.fouryouandme.researchkit.recorder.config.LocationRecorderConfig
import org.fouryouandme.researchkit.recorder.config.PedometerRecorderConfig
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.introduction.IntroductionItem

sealed class Task(
    val identifier: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration
) {

    val steps: List<Step> by lazy { buildSteps(configuration, imageConfiguration) }

    protected open fun buildSteps(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ): List<Step> = emptyList()

    companion object {

        fun byType(
            identifier: String,
            type: ETaskType,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration,
            moshi: Moshi
        ): Task =
            when (type) {
                ETaskType.WALK -> GaitTask(configuration, imageConfiguration, moshi)
                ETaskType.GAIT -> GaitTask(configuration, imageConfiguration, moshi)
                ETaskType.VIDEO_DIARY -> VideoDiary(identifier, configuration, imageConfiguration)
            }

    }

    class GaitTask(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        private val moshi: Moshi
    ) : Task("reaction_time", configuration, imageConfiguration) {

        override fun buildSteps(
            configuration: Configuration,
            imageConfiguration: ImageConfiguration
        ): List<Step> =
            listOf(
                // TODO: Remove mock
                Step.StartStep(
                    "start_1",
                    configuration,
                    "Gait and Balance",
                    "This activity measures your gait and balance as you walk and stand still. Do not continue if you cannot safely walk unassisted.",
                    "Next"
                ),
                Step.IntroductionStep(
                    "introduction_1",
                    configuration,
                    "Gait and Balance",
                    "Find a place where you can safely walk back and forth in a straight line. Try to walk continuously by turning at the ends of your path, as if you are walking around a cone.\\n\\nNext you will be instructed to turn around in a full circle, then stand still with your arms at your sides and your feet about shoulder-width apart.\n\nTap Get Started when you are ready to begin.\\nThen place your device in a pocket or bag and follow the audio instructions.",
                    imageConfiguration.pocket(),
                    "Next"
                ),
                Step.CountDownStep(
                    "countdown_1",
                    configuration,
                    "Gait and Balance",
                    5
                ),
                Step.SensorStep(
                    identifier = "active_step_walk",
                    configuration = configuration,
                    title = "Gait and Balance",
                    description = "Walk back and forth in a straight line for 30 seconds. Walk as you would normally.",
                    target = Step.SensorRecorderTarget.Steps(10),
                    recorderConfigurations = listOf(
                        PedometerRecorderConfig(moshi),
                        AccelerometerRecorderConfig(moshi, 100.toDouble()),
                        DeviceMotionRecorderConfig(moshi, 100.toDouble()),
                        LocationRecorderConfig(moshi)
                    ),
                    spokenInstruction = "Walk back and forth in a straight line for 30 seconds. Walk as you would normally.",
                    shouldPlaySoundOnFinish = true,
                    shouldVibrateOnFinish = true

                ),
                Step.SensorStep(
                    identifier = "active_step_turn",
                    configuration = configuration,
                    title = "Gait and Balance",
                    description = "Turn in a full circle and then stand still for 30 seconds",
                    target = Step.SensorRecorderTarget.Steps(10),
                    recorderConfigurations = listOf(
                        //PedometerRecorderConfig(moshi),
                        //AccelerometerRecorderConfig(moshi, 100.toDouble()),
                        //DeviceMotionRecorderConfig(moshi, 100.toDouble()),
                        //LocationRecorderConfig(moshi)
                    ),
                    spokenInstruction = "Turn in a full circle and then stand still for 30 seconds",
                    shouldPlaySoundOnFinish = true,
                    shouldVibrateOnFinish = true

                ),
                Step.IntroductionStep(
                    "introduction_completed",
                    configuration,
                    "Gait and Balance",
                    "You have completed the activity.",
                    imageConfiguration.pocket(),
                    "Next",
                ),
                Step.EndStep(
                    "end_1",
                    configuration,
                    "Activity Complete",
                    "Your data will be analyzed and you will be notified when your result are ready.",
                    "Done",
                    close = true
                )
            )

    }

    class VideoDiary(
        identifier: String,
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ) : Task(identifier, configuration, imageConfiguration) {

        override fun buildSteps(
            configuration: Configuration,
            imageConfiguration: ImageConfiguration
        ): List<Step> =
            listOf(
                Step.IntroductionListStep(
                    "introduction",
                    configuration,
                    configuration.text.videoDiary.introTitle,
                    imageConfiguration.videoDiaryIntro(),
                    configuration.text.videoDiary.introButton,
                    listOf(
                        IntroductionItem(
                            configuration.text.videoDiary.introParagraphTitleA,
                            configuration.text.videoDiary.introParagraphBodyA,
                            configuration
                        ),

                        IntroductionItem(
                            configuration.text.videoDiary.introParagraphTitleB,
                            configuration.text.videoDiary.introParagraphBodyB,
                            configuration
                        ),

                        IntroductionItem(
                            configuration.text.videoDiary.introParagraphTitleB,
                            configuration.text.videoDiary.introParagraphBodyB,
                            configuration
                        )
                    )
                ),
                Step.VideoDiaryStep(
                    "video_diary",
                    configuration,
                    title = configuration.text.videoDiary.recorderTitle,
                    titleColor = configuration.theme.secondaryColor.color(),
                    recordImage = imageConfiguration.videoDiaryRecord(),
                    pauseImage = imageConfiguration.videoDiaryPause(),
                    playImage = imageConfiguration.videoDiaryPlay(),
                    flashOnImage = imageConfiguration.videoDiaryFlashOn(),
                    flashOffImage = imageConfiguration.videoDiaryFlashOff(),
                    cameraToggleImage = imageConfiguration.videoDiaryToggleCamera(),
                    startRecordingDescription = configuration.text.videoDiary.recorderStartRecordingDescription,
                    startRecordingDescriptionColor = configuration.theme.primaryTextColor.color(),
                    timeImage = imageConfiguration.videoDiaryTime(),
                    timeColor = configuration.theme.fourthTextColor.color(),
                    timeProgressBackgroundColor = configuration.theme.deactiveColor.color(),
                    timeProgressColor = configuration.theme.activeColor.color(),
                    infoTitle = configuration.text.videoDiary.recorderInfoTitle,
                    infoTitleColor = configuration.theme.fourthTextColor.color(),
                    infoBody = configuration.text.videoDiary.recorderInfoBody,
                    infoBodyColor = configuration.theme.primaryTextColor.color(),
                    reviewTimeColor = configuration.theme.primaryTextColor.color(),
                    reviewButton = configuration.text.videoDiary.recorderReviewButton,
                    submitButton = configuration.text.videoDiary.recorderSubmitButton,
                    buttonColor = configuration.theme.primaryColorEnd.color(),
                    buttonTextColor = configuration.theme.secondaryColor.color(),
                    infoBackgroundColor = configuration.theme.secondaryColor.color(),
                    closeImage = imageConfiguration.videoDiaryClose(),
                    missingPermissionCamera = configuration.text.videoDiary.missingPermissionTitleCamera,
                    missingPermissionCameraBody = configuration.text.videoDiary.missingPermissionBodyCamera,
                    missingPermissionMic = configuration.text.videoDiary.missingPermissionTitleMic,
                    missingPermissionMicBody = configuration.text.videoDiary.missingPermissionBodyMic,
                    settings = configuration.text.videoDiary.missingPermissionBodySettings,
                    cancel = configuration.text.videoDiary.missingPermissionDiscard
                )
            )
    }

}