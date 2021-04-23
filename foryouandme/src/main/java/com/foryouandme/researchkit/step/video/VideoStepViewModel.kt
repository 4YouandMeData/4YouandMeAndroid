package com.foryouandme.researchkit.step.video

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoStepViewModel @Inject constructor(): ViewModel() {

    /* --- state --- */

    val state = MutableStateFlow(VideoState())
    val stateFlow = state as StateFlow<VideoState>

}