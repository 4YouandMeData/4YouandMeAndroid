package com.foryouandme.researchkit.step.introduction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class IntroductionViewModel @Inject constructor() : ViewModel() {

    private val state = MutableStateFlow(IntroductionState())
    val stateFlow = state as StateFlow<IntroductionState>

    /* --- step --- */

    var animationJob: Job? = null

    private suspend fun setStep(step: IntroductionStep?) {

        state.emit(state.value.copy(step = step, currentImageIndex = 0))
        animationJob?.cancel()
        if(step?.image?.size ?: 0 > 1)
            animationJob = viewModelScope.launchSafe { startCarousel() }

    }

    private suspend fun startCarousel() {
        delay(1000)
        val imagesSize = state.value.step?.image?.size ?: 0
        val nextImageIndex =
            if (state.value.currentImageIndex + 1 < imagesSize)
                state.value.currentImageIndex + 1
            else 0
        state.emit(state.value.copy(currentImageIndex = nextImageIndex))
        startCarousel()
    }

    /* --- actions --- */

    fun execute(action: IntroductionAction) {
        when (action) {
            is IntroductionAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
        }
    }

}