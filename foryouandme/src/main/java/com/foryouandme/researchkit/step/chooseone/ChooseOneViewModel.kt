package com.foryouandme.researchkit.step.chooseone

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.researchkit.step.common.QuestionItem

class ChooseOneViewModel(navigator: Navigator) :
    BaseViewModel<ChooseOneStepState, ChooseOneStepStateUpdate, Empty, Empty>(
        navigator = navigator
    ) {

    suspend fun initialize(step: ChooseOneStep, answers: List<ChooseOneAnswer>): Unit {

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

        setState(ChooseOneStepState(items)) { ChooseOneStepStateUpdate.Initialization(it.items) }

    }

    suspend fun answer(answerId: String): Unit {

        val items = state().items.map {
            if (it is ChooseOneAnswerItem) it.copy(isSelected = it.id == answerId)
            else it
        }

        setState(state().copy(items = items)) { ChooseOneStepStateUpdate.Answer(it.items) }

    }

    fun getSelectedAnswer(): ChooseOneAnswerItem? =
        state().items.firstOrNull {
            when (it) {
                is ChooseOneAnswerItem -> it.isSelected
                else -> false
            }
        } as? ChooseOneAnswerItem
}