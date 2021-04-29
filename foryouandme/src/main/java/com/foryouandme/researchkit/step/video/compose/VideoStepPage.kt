package com.foryouandme.researchkit.step.video.compose

import android.content.Context
import android.graphics.Color
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.action.permissionSettingsAction
import com.foryouandme.core.ext.errorToast
import com.foryouandme.core.ext.findWindow
import com.foryouandme.entity.camera.CameraEvent
import com.foryouandme.entity.permission.Permission
import com.foryouandme.researchkit.step.video.*
import com.foryouandme.researchkit.step.video.VideoStepAction.*
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.preview.ComposePreview
import com.foryouandme.ui.compose.toColor
import com.foryouandme.ui.compose.video.VideoPlayer
import com.foryouandme.ui.compose.video.VideoPlayerEvent
import com.foryouandme.ui.compose.window.KeepScreenOn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

@Composable
fun VideoStepPage(
    videoStepViewModel: VideoStepViewModel = viewModel(),
    onCloseClicked: () -> Unit,
    onVideoSubmitted: () -> Unit,
    close: () -> Unit
) {

    val state by videoStepViewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = videoStepViewModel) {
        videoStepViewModel.videoEvents
            .onEach {
                when (it) {
                    is VideoStepEvent.MergeError -> context.errorToast(it.error)
                    is VideoStepEvent.SubmitError -> context.errorToast(it.error)
                    VideoStepEvent.Submitted -> onVideoSubmitted()
                    is VideoStepEvent.MissingPermission -> {
                        when (it.permission) {
                            Permission.Camera ->
                                showPermissionError(
                                    context,
                                    state.step?.missingPermissionCamera.orEmpty(),
                                    state.step?.missingPermissionCameraBody.orEmpty(),
                                    state.step?.settings.orEmpty(),
                                    state.step?.cancel.orEmpty(),
                                    close
                                )
                            Permission.RecordAudio ->
                                showPermissionError(
                                    context,
                                    state.step?.missingPermissionMic.orEmpty(),
                                    state.step?.missingPermissionMicBody.orEmpty(),
                                    state.step?.settings.orEmpty(),
                                    state.step?.cancel.orEmpty(),
                                    close
                                )
                            else -> Unit
                        }
                    }
                }
            }
            .collect()
    }

    KeepScreenOn(key = videoStepViewModel)

    ForYouAndMeTheme {
        VideoStepPage(
            state = state,
            cameraEvents = videoStepViewModel.cameraEvents,
            videoPlayerEvents = videoStepViewModel.videoPlayerEvents,
            requestPermissions = { videoStepViewModel.execute(RequestPermissions) },
            onFlashClicked = { videoStepViewModel.execute(ToggleFlash) },
            onCameraClicked = { videoStepViewModel.execute(ToggleCamera) },
            onMediaButtonClicked = { videoStepViewModel.execute(PlayPause) },
            onRecordError = { videoStepViewModel.execute(HandleVideoRecordError) },
            onCloseClicked = { onCloseClicked() },
            onReviewClicked = { videoStepViewModel.execute(Merge) }
        )
    }

}

@Composable
private fun VideoStepPage(
    state: VideoStepState,
    cameraEvents: Flow<CameraEvent>,
    videoPlayerEvents: Flow<VideoPlayerEvent>,
    requestPermissions: () -> Unit = {},
    onFlashClicked: () -> Unit = {},
    onCameraClicked: () -> Unit = {},
    onMediaButtonClicked: () -> Unit = {},
    onRecordError: () -> Unit = {},
    onCloseClicked: () -> Unit = {},
    onReviewClicked: () -> Unit = {}
) {

    if (state.step != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            if(state.permissionsGranted.not())
                requestPermissions()
            else if (
                state.recordingState is RecordingState.RecordingPause ||
                state.recordingState is RecordingState.Recording
            )
                Camera(
                    cameraFlash = state.cameraFlash,
                    cameraLens = state.cameraLens,
                    cameraEvents = cameraEvents,
                    onRecordError = onRecordError,
                    modifier = Modifier.fillMaxSize()
                )
            else if (state.mergedVideoPath is LazyData.Data)
                VideoPlayer(
                    sourceUrl = state.mergedVideoPath.data,
                    videoPlayerEvents = videoPlayerEvents,
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
                    onFlashClicked = onFlashClicked,
                    cameraToggle = state.step.cameraToggleImage,
                    cameraLens = state.cameraLens,
                    onCameraClicked = onCameraClicked
                )
                MediaButton(
                    recordingState = state.recordingState,
                    pause = state.step.pauseImage,
                    play = state.step.playImage,
                    record = state.step.recordImage,
                    onMediaButtonClicked = onMediaButtonClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                VideoStepInfo(
                    recordingState = state.recordingState,
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
                    submitButton = state.step.submitButton,
                    buttonColor = state.step.buttonColor.toColor(),
                    buttonTextColor = state.step.buttonTextColor.toColor(),
                    onCloseClicked = onCloseClicked,
                    onReviewClicked = onReviewClicked
                )
            }
            Loading(
                backgroundColor = state.step.infoBackgroundColor.toColor(),
                isVisible =
                state.mergedVideoPath is LazyData.Loading ||
                        state.submit is LazyData.Loading,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun showPermissionError(
    context: Context,
    title: String,
    description: String,
    settings: String,
    cancel: String,
    close: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(description)
        .setPositiveButton(settings) { _, _ ->
            permissionSettingsAction().invoke(context)
            close()
        }
        .setNegativeButton(cancel) { _, _ -> close() }
        .setCancelable(false)
        .show()

}

@Preview
@Composable
private fun VideoStepPagePreview() {
    ForYouAndMeTheme {

        VideoStepPage(
            state = VideoStepState(
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
            cameraEvents = flow { },
            videoPlayerEvents = flow {  }
        )
    }
}