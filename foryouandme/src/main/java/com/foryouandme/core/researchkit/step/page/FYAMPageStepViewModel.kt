package com.foryouandme.core.researchkit.step.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.launchSafe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FYAMPageStepViewModel @Inject constructor(
    val imageConfiguration: ImageConfiguration
): ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(FYAMPageStepState())
    val stateFlow = state as StateFlow<FYAMPageStepState>

    /* --- step --- */

    private suspend fun setStep(step: FYAMPageStep) {
        state.emit(state.value.copy(step = step))
    }

    /* --- action --- */

    fun execute(action: FYAMPageStepAction) {
        when(action) {
            is FYAMPageStepAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
        }
    }

}