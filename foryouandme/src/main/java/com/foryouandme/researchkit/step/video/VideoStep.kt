package com.foryouandme.researchkit.step.video

import com.foryouandme.researchkit.step.Step

class VideoStep(
    identifier: String,
    val title: String,
    val titleColor: Int,
    val recordImage: Int,
    val pauseImage: Int,
    val playImage: Int,
    val flashOnImage: Int,
    val flashOffImage: Int,
    val cameraToggleImage: Int,
    val startRecordingDescription: String,
    val startRecordingDescriptionColor: Int,
    val timeImage: Int,
    val timeColor: Int,
    val timeProgressBackgroundColor: Int,
    val timeProgressColor: Int,
    val infoTitle: String,
    val infoTitleColor: Int,
    val infoFilter: String,
    val filterImage: Int,
    val infoBody: String,
    val infoBodyColor: Int,
    val reviewTimeColor: Int,
    val reviewButton: String,
    val submitButton: String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val infoBackgroundColor: Int,
    val closeImage: Int,
    val missingPermissionCamera: String,
    val missingPermissionCameraBody: String,
    val missingPermissionMic: String,
    val missingPermissionMicBody: String,
    val settings: String,
    val cancel: String,
    val videoDiaryFilter: Int,
) : Step(identifier = identifier, back = null, skip = null, view = { VideoStepFragment() })