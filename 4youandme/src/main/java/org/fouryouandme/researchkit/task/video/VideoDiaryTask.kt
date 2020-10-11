package org.fouryouandme.researchkit.task.video

import org.fouryouandme.R
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.introduction.list.IntroductionItem
import org.fouryouandme.researchkit.step.introduction.list.IntroductionListStep
import org.fouryouandme.researchkit.step.video.VideoStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers

class VideoDiaryTask(
    id: String,
    introBackgroundColor: Int,
    introFooterBackgroundColor: Int,
    introTitle: String,
    introTitleColor: Int,
    introImage: Int,
    introButton: String,
    introButtonColor: Int,
    introButtonTextColor: Int,
    introRemindButton: String,
    introRemindButtonColor: Int,
    introRemindButtonTextColor: Int,
    introList: List<IntroductionItem>,
    introShadowColor: Int,
    introToolbarColor: Int,
    videoTitle: String,
    videoTitleColor: Int,
    videoRecordImage: Int,
    videoPauseImage: Int,
    videoPlayImage: Int,
    videoFlashOnImage: Int,
    videoFlashOffImage: Int,
    videoCameraToggleImage: Int,
    videoStartRecordingDescription: String,
    videoStartRecordingDescriptionColor: Int,
    videoTimeImage: Int,
    videoTimeColor: Int,
    videoTimeProgressBackgroundColor: Int,
    videoTimeProgressColor: Int,
    videoInfoTitle: String,
    videoInfoTitleColor: Int,
    videoInfoBody: String,
    videoInfoBodyColor: Int,
    videoReviewTimeColor: Int,
    videoReviewButton: String,
    videoSubmitButton: String,
    videoButtonColor: Int,
    videoButtonTextColor: Int,
    videoInfoBackgroundColor: Int,
    videoCloseImage: Int,
    videoMissingPermissionCamera: String,
    videoMissingPermissionCameraBody: String,
    videoMissingPermissionMic: String,
    videoMissingPermissionMicBody: String,
    videoSettings: String,
    videoCancel: String,
) : Task(TaskIdentifiers.VIDEO_DIARY, id) {


    override val steps: List<Step> by lazy {

        listOf(
            IntroductionListStep(
                VIDEO_DIARY_INTRO,
                backgroundColor = introBackgroundColor,
                footerBackgroundColor = introFooterBackgroundColor,
                title = introTitle,
                titleColor = introTitleColor,
                image = introImage,
                button = introButton,
                buttonColor = introButtonColor,
                buttonTextColor = introButtonTextColor,
                remindButton = {
                    introRemindButton ?: it.getString(R.string.TASK_welcome_remind_button)
                },
                remindButtonColor = introRemindButtonColor,
                remindButtonTextColor = introRemindButtonTextColor,
                list = introList,
                shadowColor = introShadowColor,
                toolbarColor = introToolbarColor,
            ),
            VideoStep(
                VIDEO_DIARY_VIDEO,
                title = videoTitle,
                titleColor = videoTitleColor,
                recordImage = videoRecordImage,
                pauseImage = videoPauseImage,
                playImage = videoPlayImage,
                flashOnImage = videoFlashOnImage,
                flashOffImage = videoFlashOffImage,
                cameraToggleImage = videoCameraToggleImage,
                startRecordingDescription = videoStartRecordingDescription,
                startRecordingDescriptionColor = videoStartRecordingDescriptionColor,
                timeImage = videoTimeImage,
                timeColor = videoTimeColor,
                timeProgressBackgroundColor = videoTimeProgressBackgroundColor,
                timeProgressColor = videoTimeProgressColor,
                infoTitle = videoInfoTitle,
                infoTitleColor = videoInfoTitleColor,
                infoBody = videoInfoBody,
                infoBodyColor = videoInfoBodyColor,
                reviewTimeColor = videoReviewTimeColor,
                reviewButton = videoReviewButton,
                submitButton = videoSubmitButton,
                buttonColor = videoButtonColor,
                buttonTextColor = videoButtonTextColor,
                infoBackgroundColor = videoInfoBackgroundColor,
                closeImage = videoCloseImage,
                missingPermissionCamera = videoMissingPermissionCamera,
                missingPermissionCameraBody = videoMissingPermissionCameraBody,
                missingPermissionMic = videoMissingPermissionMic,
                missingPermissionMicBody = videoMissingPermissionMicBody,
                settings = videoSettings,
                cancel = videoCancel
            )

        )

    }

    companion object {

        const val VIDEO_DIARY_INTRO: String = "video_diary_intro"

        const val VIDEO_DIARY_VIDEO: String = "video_diary_video"

    }

}