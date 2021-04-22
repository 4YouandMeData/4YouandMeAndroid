package com.foryouandme.researchkit.step.introduction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class IntroductionViewModel @Inject constructor() : ViewModel() {

    private val state = MutableStateFlow(IntroductionState())
    val stateFlow = state as StateFlow<IntroductionState>

    /* --- step --- */

    private suspend fun setStep(step: IntroductionStep?) {

        state.emit(state.value.copy(step = step))

    }

    /* --- actions --- */

    fun execute(action: IntroductionAction) {
        when (action) {
            is IntroductionAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
        }
    }

}