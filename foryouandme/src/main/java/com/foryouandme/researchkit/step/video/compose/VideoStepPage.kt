package com.foryouandme.researchkit.step.video.compose

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.entity.camera.CameraEvent
import com.foryouandme.researchkit.step.video.RecordingState
import com.foryouandme.researchkit.step.video.VideoState
import com.foryouandme.researchkit.step.video.VideoStep
import com.foryouandme.researchkit.step.video.VideoStepAction.*
import com.foryouandme.researchkit.step.video.VideoStepViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.preview.ComposePreview
import com.foryouandme.ui.compose.toColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun VideoStepPage(
    videoStepViewModel: VideoStepViewModel = viewModel(),
    onCloseClicked: () -> Unit
) {

    val state by videoStepViewModel.stateFlow.collectAsState()
    ForYouAndMeTheme {
        VideoStepPage(
            state = state,
            cameraEvents = videoStepViewModel.cameraEvents,
            onFlashClicked = { videoStepViewModel.execute(ToggleFlash) },
            onCameraClicked = { videoStepViewModel.execute(ToggleCamera) },
            onMediaButtonClicked = { videoStepViewModel.execute(PlayPause) },
            onRecordError = { videoStepViewModel.execute(HandleVideoRecordError) },
            onCloseClicked = { onCloseClicked() }
        )
    }

}

@Composable
private fun VideoStepPage(
    state: VideoState,
    cameraEvents: Flow<CameraEvent>,
    onFlashClicked: () -> Unit = {},
    onCameraClicked: () -> Unit = {},
    onMediaButtonClicked: () -> Unit = {},
    onRecordError: () -> Unit = {},
    onCloseClicked: () -> Unit = {}
) {

    if (state.step != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Camera(
                cameraFlash = state.cameraFlash,
                cameraLens = state.cameraLens,
                cameraEvents = cameraEvents,
                onRecordError = { onRecordError() },
                modifier = Modifier.fillMaxSize()
            )
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))
                VideoStepHeader(
                    title = state.step.title,
                    recordingState = state.recordingState,
                    recordTimeSeconds = state.recordTimeSeconds,
                    maxRecordTimeSeconds = state.maxRecordTimeSeconds,
                    color = state.step.titleColor.toColor(),
                    flashOn = state.step.flashOnImage,
                    flashOff = state.step.flashOffImage,
                    cameraFlash = state.cameraFlash,
                    onFlashClicked = { onFlashClicked() },
                    cameraToggle = state.step.cameraToggleImage,
                    cameraLens = state.cameraLens,
                    onCameraClicked = { onCameraClicked() }
                )
                MediaButton(
                    recordingState = state.recordingState,
                    pause = state.step.pauseImage,
                    play = state.step.playImage,
                    record = state.step.recordImage,
                    onMediaButtonClicked = { onMediaButtonClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                if (state.recordingState is RecordingState.RecordingPause)
                    VideoStepRecordingInfo(
                        backgroundColor = state.step.infoBackgroundColor.toColor(),
                        title = state.step.startRecordingDescription,
                        titleColor = state.step.startRecordingDescriptionColor.toColor(),
                        closeImage = state.step.closeImage,
                        timeImage = state.step.timeImage,
                        recordTimeSeconds = state.recordTimeSeconds,
                        maxRecordTimeSeconds = state.maxRecordTimeSeconds,
                        timeColor = state.step.timeColor.toColor(),
                        progressColor = state.step.timeProgressColor.toColor(),
                        progressBackgroundColor = state.step.timeProgressBackgroundColor.toColor(),
                        infoTitle = state.step.infoTitle,
                        infoTitleColor = state.step.infoTitleColor.toColor(),
                        infoBody = state.step.infoBody,
                        infoBodyColor = state.step.infoBodyColor.toColor(),
                        reviewButton = state.step.reviewButton,
                        reviewButtonColor =state.step.buttonColor.toColor(),
                        reviewButtonTextColor = state.step.buttonTextColor.toColor(),
                        onCloseClicked = { onCloseClicked() },
                        onReviewClicked = {  }
                    )
            }
        }
    }
}

@Preview
@Composable
private fun VideoStepPagePreview() {
    ForYouAndMeTheme {

        VideoStepPage(
            state = VideoState(
                step =
                VideoStep(
                    identifier = "",
                    title = ComposePreview.title,
                    titleColor = Color.WHITE,
                    recordImage = 0,
                    pauseImage = 0,
                    playImage = 0,
                    flashOffImage = 0,
                    flashOnImage = 0,
                    cameraToggleImage = 0,
                    startRecordingDescription = ComposePreview.body,
                    startRecordingDescriptionColor = Color.WHITE,
                    timeImage = 0,
                    timeColor = Color.WHITE,
                    timeProgressBackgroundColor = Color.WHITE,
                    timeProgressColor = Color.WHITE,
                    infoTitle = ComposePreview.title,
                    infoTitleColor = Color.WHITE,
                    infoBody = ComposePreview.body,
                    infoBodyColor = Color.WHITE,
                    reviewTimeColor = Color.WHITE,
                    reviewButton = ComposePreview.button,
                    submitButton = ComposePreview.button,
                    buttonColor = Color.WHITE,
                    buttonTextColor = Color.WHITE,
                    infoBackgroundColor = Color.WHITE,
                    closeImage = 0,
                    missingPermissionCamera = ComposePreview.title,
                    missingPermissionCameraBody = ComposePreview.body,
                    missingPermissionMic = ComposePreview.title,
                    missingPermissionMicBody = ComposePreview.body,
                    settings = ComposePreview.button,
                    cancel = ComposePreview.button

                )
            ),
            cameraEvents = flow { }
        )
    }
}