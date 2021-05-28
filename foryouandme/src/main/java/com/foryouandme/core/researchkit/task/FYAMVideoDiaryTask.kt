package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.core.researchkit.step.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.video.VideoDiaryTask

class FYAMVideoDiaryTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val pages: List<Page>,
    private val welcomePage: Page,
    private val successPage: Page?,
    private val reschedule: Reschedule?
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

        welcomePage.asList(pages).mapIndexed { index, page ->

            FYAMPageStep(
                getVideoDiaryWelcomeStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO,
                index == 0 && reschedule.isEnabled()
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
            ).let { list ->

                successPage?.let {

                    list.plus(
                        FYAMPageStep(
                            getVideoDiarySuccessStepId(it.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            it,
                            EPageType.SUCCESS,
                            false
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