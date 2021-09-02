package com.foryouandme.ui.auth.onboarding.step.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    val imageConfiguration: ImageConfiguration,
    val videoConfiguration: VideoConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(VideoState())
    val stateFlow = state as StateFlow<VideoState>

    init {
        execute(VideoAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- player --- */

    private suspend fun play() {
        state.emit(state.value.copy(isPlaying = true))
    }

    private suspend fun pause() {
        state.emit(state.value.copy(isPlaying = false))
    }

    /* --- action --- */

    fun execute(action: VideoAction) {
        when (action) {
            VideoAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            VideoAction.Pause ->
                viewModelScope.launchSafe { pause() }
            VideoAction.Play ->
                viewModelScope.launchSafe { play() }
        }

    }

}