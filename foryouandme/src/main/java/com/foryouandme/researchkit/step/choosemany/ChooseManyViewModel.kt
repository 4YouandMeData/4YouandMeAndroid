package com.foryouandme.researchkit.step.choosemany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.step.chooseone.ChooseOneAnswerItem
import com.foryouandme.researchkit.step.common.QuestionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseManyViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ChooseManyStepStateUpdate>
) : ViewModel() {

    /* --- state -- */

    var state: ChooseManyStepState = ChooseManyStepState()
        private set

    /* --- flow --- */

    val stateUpdates = stateUpdateFlow.stateUpdates

    /* --- initialize --- */

    private suspend fun initialize(step: ChooseManyStep, answers: List<ChooseManyAnswer>) {

        val items =
            listOf(QuestionItem(step.questionId, step.question, step.questionColor, step.image))
                .plus(
                    answers.map {

                        ChooseManyAnswerItem(
                            it.id,
                            it.text,
                            false,
                            it.textColor,
                            it.buttonColor,
                            it.isNone,
                            if (it.isOther) "" else null,
                            it.otherPlaceholder
                        )


                    }
                )

        state = state.copy(items = items)
        stateUpdateFlow.update(ChooseManyStepStateUpdate.Initialization(items))

    }

    /* --- answer --- */

    private suspend fun answer(answerId: String) {

        val selectedAnswerIsNone =
            state.items
                .filterIsInstance<ChooseManyAnswerItem>()
                .firstOrNull { it.id == answerId }
                ?.isNone ?: false

        val items =
            state.items.map {
                if (it is ChooseManyAnswerItem) {
                    when (it.id) {
                        answerId -> it.copy(isSelected = it.isSelected.not())
                        else -> {

                            val isSelected =
                                when {
                                    selectedAnswerIsNone -> false
                                    it.isNone -> false
                                    else -> it.isSelected
                                }


                            it.copy(isSelected = isSelected)
                        }
                    }
                } else it
            }

        state = state.copy(items = items)
        stateUpdateFlow.update(ChooseManyStepStateUpdate.Answer(items))

    }

    private suspend fun answerText(answerId: String, text: String) {

        val items = state.items.map {
            if (it is ChooseManyAnswerItem && it.id == answerId) it.copy(otherText = text)
            else it
        }

        state = state.copy(items = items)
        stateUpdateFlow.update(ChooseManyStepStateUpdate.Answer(items))

    }

    fun getSelectedAnswers(): List<ChooseManyAnswerItem> =
        state.items
            .mapNotNull { it as? ChooseManyAnswerItem }
            .filter { it.isSelected }

    /* --- state event --- */

    fun execute(stateEvent: ChooseManyStepStateEvent) {

        when (stateEvent) {
            is ChooseManyStepStateEvent.Answer ->
                viewModelScope.launchSafe { answer(stateEvent.id) }
            is ChooseManyStepStateEvent.Initialize ->
                viewModelScope.launchSafe { initialize(stateEvent.step, stateEvent.answers) }
            is ChooseManyStepStateEvent.AnswerTextChange ->
                viewModelScope.launchSafe { answerText(stateEvent.answerId, stateEvent.text) }
        }

    }

}