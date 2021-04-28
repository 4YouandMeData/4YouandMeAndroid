package com.foryouandme.researchkit.step.video

import com.foryouandme.core.arch.LazyData
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.camera.CameraFlash
import com.foryouandme.entity.camera.CameraLens
import java.io.File

data class VideoStepState(
    val step: VideoStep? = null,
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

sealed class VideoStateUpdate {

    data class RecordTime(
        val time: Long,
    ) : VideoStateUpdate()

    object Recording : VideoStateUpdate()
    object Flash : VideoStateUpdate()
    object Camera : VideoStateUpdate()

}

sealed class RecordingState {

    object Recording : RecordingState()
    object Merged : RecordingState()
    object RecordingPause : RecordingState()
    object Review : RecordingState()
    object ReviewPause : RecordingState()
    object Uploaded : RecordingState()

}

sealed class VideoError {

    object Recording : VideoError()
    object Merge : VideoError()
    object Upload : VideoError()

}

sealed class VideoLoading {

    object Merge : VideoLoading()
    object Upload : VideoLoading()

}

sealed class VideoStateEvent {

    object ToggleCamera : VideoStateEvent()
    object ToggleFlash : VideoStateEvent()

    data class Merge(
        val videoDirectoryPath: String,
        val mergeDirectory: String,
        val videoMergeFileName: String
    ): VideoStateEvent()

    data class Submit(val taskId: String, val file: File) : VideoStateEvent()
    object HandleRecordError : VideoStateEvent()
    object Pause : VideoStateEvent()
    data class Record(val filePath: String): VideoStateEvent()
    object ReviewPlay: VideoStateEvent()
    object ReviewPause: VideoStateEvent()

}

sealed class VideoStepAction {

    data class SetStep(val step: VideoStep?): VideoStepAction()

    object ToggleCamera : VideoStepAction()
    object ToggleFlash : VideoStepAction()

    object PlayPause: VideoStepAction()

    object HandleVideoRecordError: VideoStepAction()

    object Merge: VideoStepAction()

    data class Submit(val taskId: String): VideoStepAction()

}

sealed class VideoStepEvent {

    data class MergeError(val error: ForYouAndMeException): VideoStepEvent()
    data class SubmitError(val error: ForYouAndMeException): VideoStepEvent()
    object Submitted: VideoStepEvent()

}