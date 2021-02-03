package com.foryouandme.researchkit.step.chooseone

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.step.common.QuestionItem
import kotlinx.coroutines.flow.SharedFlow

class ChooseOneViewModel @ViewModelInject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ChooseOneStepStateUpdate>
): ViewModel() {

    /* --- state --- */

    var state: ChooseOneStepState = ChooseOneStepState()
        private set

    /* --- flow --- */

    val stateUpdate: SharedFlow<ChooseOneStepStateUpdate> = stateUpdateFlow.stateUpdates

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
                            it.buttonColor
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
        }

    }

}