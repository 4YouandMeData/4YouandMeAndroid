package com.foryouandme.researchkit.step.video

import com.foryouandme.core.arch.LazyData
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.permission.Permission
import com.foryouandme.ui.compose.camera.CameraFlash
import com.foryouandme.ui.compose.camera.CameraLens
import java.io.File

data class VideoStepState(
    val step: VideoStep? = null,
    val permissionsGranted: Boolean = false,
    val startRecordTimeSeconds: Long = 0,
    val recordTimeSeconds: Long = 0,
    val maxRecordTimeSeconds: Long = 120,
    val lastRecordedFilePath: String? = null,
    val recordingState: RecordingState = RecordingState.RecordingPause,
    val cameraFlash: CameraFlash = CameraFlash.Off,
    val cameraLens: CameraLens = CameraLens.Back,
    val mergedVideoPath: LazyData<String> = LazyData.Empty,
    val submit: LazyData<Unit> = LazyData.Empty
)

sealed class RecordingState {

    object Recording : RecordingState()
    object Merged : RecordingState()
    object RecordingPause : RecordingState()
    object Review : RecordingState()
    object ReviewPause : RecordingState()
    object Uploaded : RecordingState()

}

sealed class VideoStepAction {

    data class SetStep(val step: VideoStep?): VideoStepAction()
    object RequestPermissions: VideoStepAction()
    object ToggleCamera : VideoStepAction()
    object ToggleFlash : VideoStepAction()
    object PlayPause: VideoStepAction()
    object HandleVideoRecordError: VideoStepAction()
    object Merge: VideoStepAction()
    data class Submit(val taskId: String): VideoStepAction()

}

sealed class VideoStepEvent {

    data class MissingPermission(val permission: Permission): VideoStepEvent()
    data class MergeError(val error: ForYouAndMeException): VideoStepEvent()
    data class SubmitError(val error: ForYouAndMeException): VideoStepEvent()
    object Submitted: VideoStepEvent()

}