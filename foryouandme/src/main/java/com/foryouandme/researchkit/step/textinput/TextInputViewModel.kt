package com.foryouandme.researchkit.step.textinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class TextInputViewModel @Inject constructor(
) : ViewModel() {

    /* --- state -- */

    private val state = MutableStateFlow(TextInputState())
    val stateFlow = state as StateFlow<TextInputState>

    /* --- events --- */

    private val events = MutableSharedFlow<UIEvent<TextInputEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<TextInputEvent>>

    /* --- step --- */

    private suspend fun setStep(step: TextInputStep) {

        state.emit(state.value.copy(step = step))

    }

    /* --- text --- */

    private suspend fun setText(text: String) {

        state.emit(
            state.value.copy(
                text = text,
                canGoNext = text.isNotEmpty()
            )
        )

    }

    /* --- next --- */

    private suspend fun next() {

        events.emit(TextInputEvent.Next(getResult()).toUIEvent())

    }

    /* --- result --- */

    private fun getResult(): SingleStringAnswerResult? {

        val step = state.value.step

        return if (step != null)
            SingleStringAnswerResult(
                step.identifier,
                state.value.start,
                ZonedDateTime.now(),
                step.questionId,
                state.value.text
            )
        else null

    }

    /* --- action --- */

    fun execute(action: TextInputAction) {

        when (action) {
            is TextInputAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is TextInputAction.SetText ->
                viewModelScope.launchSafe { setText(action.text) }
            TextInputAction.Next ->
                viewModelScope.launchSafe { next() }
        }

    }

}