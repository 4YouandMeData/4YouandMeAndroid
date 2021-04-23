package com.foryouandme.researchkit.step.video.compose

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.researchkit.step.video.VideoState
import com.foryouandme.researchkit.step.video.VideoStep
import com.foryouandme.researchkit.step.video.VideoStepAction.ToggleCamera
import com.foryouandme.researchkit.step.video.VideoStepAction.ToggleFlash
import com.foryouandme.researchkit.step.video.VideoStepViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.preview.ComposePreview
import com.foryouandme.ui.compose.toColor

@Composable
fun VideoStepPage(videoStepViewModel: VideoStepViewModel = viewModel()) {

    val state by videoStepViewModel.stateFlow.collectAsState()
    ForYouAndMeTheme {
        VideoStepPage(
            state = state,
            onFlashClicked = { videoStepViewModel.execute(ToggleFlash) },
            onCameraClicked = { videoStepViewModel.execute(ToggleCamera) }
        )
    }

}

@Composable
private fun VideoStepPage(
    state: VideoState,
    onFlashClicked: () -> Unit = {},
    onCameraClicked: () -> Unit = {}
) {

    if (state.step != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Camera(
                isFlashEnabled = state.isFlashEnabled,
                isBackCameraToggled = state.isBackCameraToggled,
                modifier = Modifier.fillMaxSize()
            )
            VideoStepHeader(
                title = state.step.title,
                recordingState = state.recordingState,
                recordTimeSeconds = state.recordTimeSeconds,
                maxRecordTimeSeconds = state.maxRecordTimeSeconds,
                color = state.step.titleColor.toColor(),
                flashOn = state.step.flashOnImage,
                flashOff = state.step.flashOffImage,
                isFlashEnabled = state.isFlashEnabled,
                onFlashClicked = { onFlashClicked() },
                cameraToggle = state.step.cameraToggleImage,
                isBackCameraToggled = state.isBackCameraToggled,
                onCameraClicked = { onCameraClicked() }
            )
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
            )

        )
    }
}