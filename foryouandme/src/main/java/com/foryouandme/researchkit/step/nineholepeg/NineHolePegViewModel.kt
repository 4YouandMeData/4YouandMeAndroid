package com.foryouandme.researchkit.step.nineholepeg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NineHolePegViewModel @Inject constructor() : ViewModel() {

    private val state = MutableStateFlow(NineHolePegState())
    val stateFlow = state as StateFlow<NineHolePegState>

    /* --- step --- */

    private suspend fun setStep(step: NineHolePegStep?) {
        state.emit(state.value.copy(step = step))
    }

    /* --- dragging --- */

    private suspend fun setDragging(isDragging: Boolean) {
        state.emit(state.value.copy(isDragging = isDragging))
    }

    /* --- state event --- */

    fun execute(stateEvent: NineHolePegSateEvent) {
        when(stateEvent) {
            is NineHolePegSateEvent.SetStep ->
                viewModelScope.launchSafe { setStep(stateEvent.step) }
            is NineHolePegSateEvent.SetDragging ->
                viewModelScope.launchSafe { setDragging(stateEvent.isDragging) }
        }
    }

}