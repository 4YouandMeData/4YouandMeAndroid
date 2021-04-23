package com.foryouandme.researchkit.step.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoStepViewModel @Inject constructor(): ViewModel() {

    /* --- state --- */

    val state = MutableStateFlow(VideoState())
    val stateFlow = state as StateFlow<VideoState>

    /* --- step --- */

    private suspend fun setStep(step: VideoStep?) {
        state.emit(state.value.copy(step = step))
    }

    /* --- flash --- */

    private suspend fun setFlash(flash: Boolean) {
        state.emit(state.value.copy(isFlashEnabled = flash))
    }

    /* --- camera --- */

    private suspend fun setCamera(isBackCameraToggled: Boolean) {
        state.emit(
            state.value.copy(
                isBackCameraToggled = isBackCameraToggled,
                // disable the flash when the front camera is toggled
                isFlashEnabled = if(isBackCameraToggled) false else state.value.isFlashEnabled
            )
        )
    }

    /* --- actions ---- */

    fun execute(action: VideoStepAction) {
        when(action) {
            is VideoStepAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            VideoStepAction.ToggleCamera ->
                viewModelScope.launchSafe { setCamera(state.value.isBackCameraToggled.not()) }
            VideoStepAction.ToggleFlash ->
                viewModelScope.launchSafe { setFlash(state.value.isFlashEnabled.not()) }
        }
    }

}