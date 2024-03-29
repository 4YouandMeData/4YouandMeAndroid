package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.step.introduction.list.IntroductionItem
import com.foryouandme.researchkit.task.video.VideoDiaryTask

suspend fun FYAMTaskConfiguration.buildVideoDiary(
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
        introBackImage = imageConfiguration.back(),
        introBackgroundColor = secondary,
        introFooterBackgroundColor = secondary,
        introTitle = configuration.text.videoDiary.introTitle,
        introTitleColor = primaryText,
        introImage = imageConfiguration.videoDiaryIntro(),
        introButton = configuration.text.videoDiary.introButton,
        introButtonColor = primaryEnd,
        introButtonTextColor = secondary,
        introRemindButton = configuration.text.task.remindButton,
        introRemindButtonColor = secondary,
        introRemindButtonTextColor = primaryEnd,
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
        videoInfoFilter = configuration.text.videoDiary.recorderInfoFilter,
        videoFilterImage = imageConfiguration.videoFilterImage(),
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
        videoCancel = configuration.text.videoDiary.missingPermissionDiscard,
        videoFilterToggleImage = imageConfiguration.videoDiaryFilter(),
    )
}

