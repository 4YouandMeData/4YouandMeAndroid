package com.foryouandme.researchkit.step.choosemany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.result.AnswerResult
import com.foryouandme.researchkit.result.MultipleAnswerResult
import com.foryouandme.researchkit.step.choosemany.compose.ChooseManyAnswerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ChooseManyViewModel @Inject constructor(
) : ViewModel() {

    /* --- state -- */

    private val state = MutableStateFlow(ChooseManyState())
    val stateFlow = state as StateFlow<ChooseManyState>

    /* --- events --- */

    private val events = MutableSharedFlow<UIEvent<ChooseManyEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<ChooseManyEvent>>

    /* --- step --- */

    private suspend fun setStep(step: ChooseManyStep) {

        val answers =
            step.values.map {
                ChooseManyAnswerData(
                    answer = it,
                    otherText = if (it.isOther) "" else null,
                    isSelected = false
                )
            }

        state.emit(state.value.copy(step = step, answers = answers))

    }

    /* --- answer --- */

    private suspend fun answer(answerId: String) {

        val selectedAnswerIsNone =
            state
                .value
                .answers
                .firstOrNull { it.answer.id == answerId }
                ?.answer
                ?.isNone ?: false

        val answers =
            state.value.answers.map {
                when (it.answer.id) {
                    answerId -> it.copy(isSelected = it.isSelected.not())
                    else -> {
                        val isSelected =
                            when {
                                selectedAnswerIsNone -> false
                                it.answer.isNone -> false
                                else -> it.isSelected
                            }
                        it.copy(isSelected = isSelected)
                    }
                }
            }

        state.emit(
            state.value.copy(
                answers = answers,
                canGoNext = answers.firstOrNull { it.isSelected } != null
            )
        )

    }

    private suspend fun answerText(answerId: String, text: String) {

        val answers = state.value.answers.map {
            if (it.answer.id == answerId) it.copy(otherText = text)
            else it
        }

        state.emit(state.value.copy(answers = answers))

    }

    /* --- skip --- */

    private suspend fun checkSkip() {

        val item = state.value.answers.firstOrNull { it.isSelected }

        if (item != null) {

            val skip = state.value.step?.skips?.firstOrNull { it.answerId == item.answer.id }
            if (skip != null)
                events.emit(ChooseManyEvent.Skip(getResult(), skip.target).toUIEvent())
            else
                events.emit(ChooseManyEvent.Next(getResult()).toUIEvent())
        }

    }

    /* --- result --- */

    private fun getResult(): MultipleAnswerResult? {

        val step = state.value.step
        val answers = state.value.answers.filter { it.isSelected }

        return if (step != null && answers.isNotEmpty())
            MultipleAnswerResult(
                step.identifier,
                state.value.start,
                ZonedDateTime.now(),
                step.questionId,
                answers.map { AnswerResult(it.answer.id, it.otherText) }
            )
        else null

    }

    /* --- state event --- */

    fun execute(action: ChooseManyAction) {

        when (action) {
            is ChooseManyAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is ChooseManyAction.Answer ->
                viewModelScope.launchSafe { answer(action.id) }
            is ChooseManyAction.AnswerTextChange ->
                viewModelScope.launchSafe { answerText(action.answerId, action.text) }
            ChooseManyAction.Next ->
                viewModelScope.launchSafe { checkSkip() }
        }

    }

}