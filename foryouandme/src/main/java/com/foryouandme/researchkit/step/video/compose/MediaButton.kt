package com.foryouandme.researchkit.step.video.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.researchkit.step.video.RecordingState

@Composable
fun MediaButton(
    recordingState: RecordingState,
    pause: Int,
    play: Int,
    record: Int,
    modifier: Modifier = Modifier,
    onMediaButtonClicked: () -> Unit = {},
) {

    val mediaImage = remember(recordingState, pause, play, record) {
        getMediaImage(recordingState, pause, play, record)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (mediaImage != null)
            Image(
                painterResource(id = mediaImage),
                contentDescription = "",
                modifier =
                Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .clickable { onMediaButtonClicked() }
            )
    }

}

private fun getMediaImage(
    recordingState: RecordingState,
    pause: Int,
    play: Int,
    record: Int
): Int? =
    when (recordingState) {
        RecordingState.Recording,
        RecordingState.Review -> pause
        RecordingState.RecordingPause -> record
        RecordingState.ReviewPause -> play
        else -> null
    }

@Preview
@Composable
private fun MediaButtonPreview() {
    MediaButton(
        recordingState = RecordingState.Recording,
        pause = 0,
        play = 0,
        record = 0,
        modifier = Modifier.fillMaxSize()
    )
}