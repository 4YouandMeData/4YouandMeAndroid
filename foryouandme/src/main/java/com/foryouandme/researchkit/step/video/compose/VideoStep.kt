package com.foryouandme.researchkit.step.video.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.researchkit.step.video.VideoState
import com.foryouandme.researchkit.step.video.VideoStepViewModel

@Composable
fun VideoStep(videoStepViewModel: VideoStepViewModel = viewModel()) {

    val state by videoStepViewModel.stateFlow.collectAsState()
    VideoStep(videoState = state)

}

@Composable
private fun VideoStep(videoState: VideoState) {
    Camera(modifier = Modifier.fillMaxSize())
}

@Preview
@Composable
private fun VideoStepPreview() {
    VideoStep(videoState = VideoState())
}