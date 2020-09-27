package org.fouryouandme.core.arch.deps

import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.step.introduction.list.IntroductionItem
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskBuilder
import org.fouryouandme.researchkit.task.TaskIdentifiers
import org.fouryouandme.researchkit.task.video.VideoDiaryTask

class FYAMTaskBuilder(
    private val configurationModule: ConfigurationModule,
    private val imageConfiguration: ImageConfiguration
) : TaskBuilder() {

    override suspend fun build(type: String, id: String): Task? {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst).orNull()

        return configuration?.let {

            when (type) {
                TaskIdentifiers.VIDEO_DIARY ->
                    buildVideoDiary(id, it, imageConfiguration)
                else -> null

            }

        }
    }

    private suspend fun buildVideoDiary(
        id: String,
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ): VideoDiaryTask {

        val secondary =
            configuration.theme.secondaryColor.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val primaryEnd =
            configuration.theme.primaryColorEnd.color()

        val fourthText =
            configuration.theme.fourthTextColor.color()

        val active =
            configuration.theme.activeColor.color()

        val deactive =
            configuration.theme.deactiveColor.color()

        return VideoDiaryTask(
            id = id,
            introBackgroundColor = secondary,
            introFooterBackgroundColor = secondary,
            introTitle = configuration.text.videoDiary.introTitle,
            introTitleColor = primaryText,
            introImage = imageConfiguration.videoDiaryIntro(),
            introButton = configuration.text.videoDiary.introButton,
            introButtonColor = primaryEnd,
            introButtonTextColor = secondary,
            introList =
            listOf(
                IntroductionItem(
                    configuration.text.videoDiary.introParagraphTitleA,
                    fourthText,
                    configuration.text.videoDiary.introParagraphBodyA,
                    primaryText
                ),

                IntroductionItem(
                    configuration.text.videoDiary.introParagraphTitleB,
                    fourthText,
                    configuration.text.videoDiary.introParagraphBodyB,
                    primaryText
                ),

                IntroductionItem(
                    configuration.text.videoDiary.introParagraphTitleB,
                    fourthText,
                    configuration.text.videoDiary.introParagraphBodyB,
                    primaryText
                )
            ),
            introShadowColor = primaryText,
            introToolbarColor = secondary,
            videoTitle = configuration.text.videoDiary.recorderTitle,
            videoTitleColor = secondary,
            videoRecordImage = imageConfiguration.videoDiaryRecord(),
            videoPauseImage = imageConfiguration.videoDiaryPause(),
            videoPlayImage = imageConfiguration.videoDiaryPlay(),
            videoFlashOnImage = imageConfiguration.videoDiaryFlashOn(),
            videoFlashOffImage = imageConfiguration.videoDiaryFlashOff(),
            videoCameraToggleImage = imageConfiguration.videoDiaryToggleCamera(),
            videoStartRecordingDescription = configuration.text.videoDiary.recorderStartRecordingDescription,
            videoStartRecordingDescriptionColor = primaryText,
            videoTimeImage = imageConfiguration.videoDiaryTime(),
            videoTimeColor = fourthText,
            videoTimeProgressBackgroundColor = deactive,
            videoTimeProgressColor = active,
            videoInfoTitle = configuration.text.videoDiary.recorderInfoTitle,
            videoInfoTitleColor = fourthText,
            videoInfoBody = configuration.text.videoDiary.recorderInfoBody,
            videoInfoBodyColor = primaryText,
            videoReviewTimeColor = primaryText,
            videoReviewButton = configuration.text.videoDiary.recorderReviewButton,
            videoSubmitButton = configuration.text.videoDiary.recorderSubmitButton,
            videoButtonColor = primaryEnd,
            videoButtonTextColor = secondary,
            videoInfoBackgroundColor = secondary,
            videoCloseImage = imageConfiguration.videoDiaryClose(),
            videoMissingPermissionCamera = configuration.text.videoDiary.missingPermissionTitleCamera,
            videoMissingPermissionCameraBody = configuration.text.videoDiary.missingPermissionBodyCamera,
            videoMissingPermissionMic = configuration.text.videoDiary.missingPermissionTitleMic,
            videoMissingPermissionMicBody = configuration.text.videoDiary.missingPermissionBodyMic,
            videoSettings = configuration.text.videoDiary.missingPermissionBodySettings,
            videoCancel = configuration.text.videoDiary.missingPermissionDiscard
        )
    }
}