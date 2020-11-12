package com.foryouandme.researchkit.step.choosemany

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.researchkit.step.common.QuestionItem

class ChooseManyViewModel(navigator: Navigator) :
    BaseViewModel<ChooseManyStepState, ChooseManyStepStateUpdate, Empty, Empty>(
        navigator = navigator
    ) {

    suspend fun initialize(step: ChooseManyStep, answers: List<ChooseManyAnswer>): Unit {

        val items =
            listOf(QuestionItem(step.questionId, step.question, step.questionColor, step.image))
                .plus(
                    answers.map {

                        ChooseManyAnswerItem(
                            it.id,
                            it.text,
                            false,
                            it.textColor,
                            it.buttonColor
                        )


                    }
                )

        setState(ChooseManyStepState(items)) {
            ChooseManyStepStateUpdate.Initialization(
                it.items
            )
        }

    }

    suspend fun answer(answerId: String): Unit {

        val items = state().items.map {
            if (it is ChooseManyAnswerItem) {
                if (it.id == answerId) it.copy(isSelected = it.isSelected.not())
                else it
            } else it
        }

        setState(state().copy(items = items)) { ChooseManyStepStateUpdate.Answer(it.items) }

    }

    fun getSelectedAnswers(): List<ChooseManyAnswerItem> =
        state().items
            .mapNotNull { it as? ChooseManyAnswerItem }
            .filter { it.isSelected }
}