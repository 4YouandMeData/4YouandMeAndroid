package org.fouryouandme.core.researchkit.task

import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.researchkit.step.FYAMPageStep
import org.fouryouandme.core.view.page.EPageType
import org.fouryouandme.researchkit.step.Back
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers
import org.fouryouandme.researchkit.task.video.VideoDiaryTask

class FYAMVideoDiaryTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val welcomePage: Page,
    private val successPage: Page?
) : Task(TaskIdentifiers.VIDEO_DIARY, id) {

    override val steps: List<Step> by lazy {

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

        welcomePage.asList().mapIndexed { _, page ->
            FYAMPageStep(
                getVideoDiaryWelcomeStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO
            )
        }.plus(
            VideoDiaryTask.getVideoDiaryCoreSteps(
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
            ).pipe { list ->

                successPage?.let {

                    list.plus(
                        FYAMPageStep(
                            getVideoDiarySuccessStepId(it.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            it,
                            EPageType.SUCCESS
                        )
                    )

                } ?: list

            }
        )

    }

    private fun getVideoDiaryWelcomeStepId(introId: String): String =
        "${VideoDiaryTask.VIDEO_DIARY_INTRO}_${introId}"

    private fun getVideoDiarySuccessStepId(successId: String): String =
        "video_diary_end_${successId}"

}