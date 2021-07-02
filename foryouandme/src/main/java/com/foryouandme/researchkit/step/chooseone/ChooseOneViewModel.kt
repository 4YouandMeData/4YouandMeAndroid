package com.foryouandme.researchkit.step.chooseone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.result.AnswerResult
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.step.chooseone.compose.ChooseOneAnswerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ChooseOneViewModel @Inject constructor(
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(ChooseOneState())
    val stateFlow = state as StateFlow<ChooseOneState>

    /* --- event --- */

    private val events = MutableSharedFlow<UIEvent<ChooseOneEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<ChooseOneEvent>>

    /* --- step --- */

    private suspend fun setStep(step: ChooseOneStep) {

        val items =
            step.values.map {
                ChooseOneAnswerData(
                    answer = it,
                    otherText = if (it.isOther) "" else null,
                    isSelected = false,
                    selectedColor = it.selectedColor.getColor(),
                    unselectedColor = it.unselectedColor.getColor(),
                    textColor = it.textColor.getColor(),
                )
            }

        state.emit(state.value.copy(step = step, answers = items))

    }

    /* --- answer --- */

    private suspend fun answer(answerId: String) {

        val items = state.value.answers.map {
            it.copy(isSelected = it.answer.id == answerId)
        }

        state.emit(state.value.copy(answers = items, canGoNext = true))

    }

    private suspend fun answerText(answerId: String, text: String) {

        val items = state.value.answers.map {
            if (it.answer.id == answerId) it.copy(otherText = text) else it
        }

        state.emit(state.value.copy(answers = items))

    }

    /* --- skip --- */

    private suspend fun checkSkip() {

        val item = state.value.answers.firstOrNull { it.isSelected }

        if (item != null) {

            val skip = state.value.step?.skips?.firstOrNull { it.answerId == item.answer.id }
            if (skip != null)
                events.emit(ChooseOneEvent.Skip(getResult(), skip.target).toUIEvent())
            else
                events.emit(ChooseOneEvent.Next(getResult()).toUIEvent())
        }

    }

    /* --- result --- */

    private fun getResult(): SingleAnswerResult? {

        val step = state.value.step
        val item = state.value.answers.firstOrNull { it.isSelected }

        return if (step != null && item != null)
            SingleAnswerResult(
                step.identifier,
                state.value.start,
                ZonedDateTime.now(),
                step.questionId,
                AnswerResult(item.answer.id, item.otherText)
            )
        else null

    }

    /* --- action--- */

    fun execute(action: ChooseOneAction) {

        when (action) {
            is ChooseOneAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is ChooseOneAction.Answer ->
                viewModelScope.launchSafe { answer(action.answerId) }
            is ChooseOneAction.AnswerTextChange ->
                viewModelScope.launchSafe { answerText(action.answerId, action.text) }
            ChooseOneAction.Next ->
                viewModelScope.launchSafe { checkSkip() }
        }

    }

}