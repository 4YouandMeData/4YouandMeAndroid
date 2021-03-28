package com.foryouandme.researchkit.step.chooseone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.step.common.QuestionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class ChooseOneViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ChooseOneStepStateUpdate>
): ViewModel() {

    /* --- state --- */

    var state: ChooseOneStepState = ChooseOneStepState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates

    /* --- initialize --- */

    private suspend fun initialize(step: ChooseOneStep, answers: List<ChooseOneAnswer>) {

        val items =
            listOf(QuestionItem(step.questionId, step.question, step.questionColor, step.image))
                .plus(
                    answers.map {

                        ChooseOneAnswerItem(
                            it.id,
                            it.text,
                            false,
                            it.textColor,
                            it.buttonColor,
                            if(it.isOther) "" else null,
                            it.otherPlaceholder
                        )
                    }
                )

        state = state.copy(items = items)
        stateUpdateFlow.update(ChooseOneStepStateUpdate.Initialization(items))

    }

    /* --- answer --- */

    private suspend fun answer(answerId: String) {

        val items = state.items.map {
            if (it is ChooseOneAnswerItem) it.copy(isSelected = it.id == answerId)
            else it
        }

        state = state.copy(items = items)
        stateUpdateFlow.update(ChooseOneStepStateUpdate.Answer(items))

    }

    private suspend fun answerText(answerId: String, text: String) {

        val items = state.items.map {
            if (it is ChooseOneAnswerItem && it.id == answerId) it.copy(otherText = text)
            else it
        }

        state = state.copy(items = items)
        stateUpdateFlow.update(ChooseOneStepStateUpdate.Answer(items))

    }

    fun getSelectedAnswer(): ChooseOneAnswerItem? =
        state.items.firstOrNull {
            when (it) {
                is ChooseOneAnswerItem -> it.isSelected
                else -> false
            }
        } as? ChooseOneAnswerItem

    /* --- state event --- */

    fun execute(stateEvent: ChooseOneStepStateEvent) {

        when(stateEvent) {
            is ChooseOneStepStateEvent.Initialize ->
                viewModelScope.launchSafe { initialize(stateEvent.step, stateEvent.answers) }
            is ChooseOneStepStateEvent.Answer ->
                viewModelScope.launchSafe { answer(stateEvent.answerId) }
            is ChooseOneStepStateEvent.AnswerTextChange ->
                viewModelScope.launchSafe { answerText(stateEvent.answerId, stateEvent.text) }
        }

    }

}