package com.foryouandme.researchkit.step.video.compose

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.camera.CameraFlash
import com.foryouandme.entity.camera.CameraFlash.*
import com.foryouandme.entity.camera.CameraLens
import com.foryouandme.entity.camera.CameraLens.*
import com.foryouandme.researchkit.step.video.RecordingState
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.preview.ComposePreview
import com.google.accompanist.coil.CoilImage

@Composable
fun VideoStepHeader(
    title: String,
    recordingState: RecordingState,
    recordTimeSeconds: Long,
    maxRecordTimeSeconds: Long,
    color: Color,
    flashOn: Int,
    flashOff: Int,
    cameraFlash: CameraFlash,
    onFlashClicked: () -> Unit = {},
    cameraToggle: Int,
    cameraLens: CameraLens,
    onCameraClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val headerLabel = remember(title, recordingState, recordTimeSeconds, maxRecordTimeSeconds) {
        getTitle(title, recordingState, recordTimeSeconds, maxRecordTimeSeconds)
    }

    val canShowFlashButton = remember(recordingState, cameraLens) {
        canShowFlashToggle(recordingState, cameraLens)
    }

    val canShowCameraButton = remember(recordingState) {
        canShowCameraToggle(recordingState)
    }

    Row(
        modifier = Modifier.fillMaxWidth().then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Box(modifier = Modifier.size(30.dp, 30.dp)) {
            if (canShowFlashButton)
                CoilImage(
                    data = if (cameraFlash is On) flashOn else flashOff,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onFlashClicked() }
                )
        }
        Text(
            text = headerLabel,
            style = MaterialTheme.typography.h2,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
        )
        Box(modifier = Modifier.size(30.dp, 30.dp)) {
            if (canShowCameraButton)
                CoilImage(
                    data = cameraToggle,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onCameraClicked() }
                )
        }
        Spacer(modifier = Modifier.width(20.dp))
    }

}

private fun getTitle(
    title: String,
    recordingState: RecordingState,
    recordTimeSeconds: Long,
    maxRecordTimeSeconds: Long,
): String =

    when (recordingState) {
        RecordingState.Recording -> {

            val currentRecordTime =
                DateUtils.formatElapsedTime(recordTimeSeconds)
            val maxRecordTime =
                DateUtils.formatElapsedTime(maxRecordTimeSeconds)

            "$currentRecordTime/$maxRecordTime"

        }
        RecordingState.RecordingPause,
        RecordingState.Review -> title
        RecordingState.Merged,
        RecordingState.ReviewPause,
        RecordingState.Uploaded -> ""
    }

private fun canShowFlashToggle(
    recordingState: RecordingState,
    cameraLens: CameraLens
): Boolean =
    when (recordingState) {
        RecordingState.Recording,
        RecordingState.RecordingPause -> cameraLens is Back
        else -> false
    }

private fun canShowCameraToggle(
    recordingState: RecordingState
): Boolean =
    when (recordingState) {
        RecordingState.RecordingPause -> true
        else -> false
    }

@Preview
@Composable
private fun VideoStepHeaderPreviewPause() {
    ForYouAndMeTheme {

        VideoStepHeader(
            title = ComposePreview.title,
            recordingState = RecordingState.RecordingPause,
            recordTimeSeconds = 50,
            maxRecordTimeSeconds = 120,
            color = Color.White,
            flashOn = 0,
            flashOff = 0,
            cameraFlash = On,
            cameraToggle = 0,
            cameraLens = Back
        )

    }
}

@Preview
@Composable
private fun VideoStepHeaderPreviewRecording() {
    ForYouAndMeTheme {

        VideoStepHeader(
            title = ComposePreview.title,
            recordingState = RecordingState.Recording,
            recordTimeSeconds = 50,
            maxRecordTimeSeconds = 120,
            color = Color.White,
            flashOn = 0,
            flashOff = 0,
            cameraFlash = On,
            cameraToggle = 0,
            cameraLens = Back
        )
    }
}