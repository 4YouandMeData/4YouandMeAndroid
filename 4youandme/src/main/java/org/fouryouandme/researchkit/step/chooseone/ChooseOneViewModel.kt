package org.fouryouandme.researchkit.step.chooseone

import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.navigation.Navigator

class ChooseOneViewModel(navigator: Navigator) :
    BaseViewModel<ChooseOneStepState, ChooseOneStepStateUpdate, Empty, Empty>(
        navigator = navigator
    ) {

    suspend fun initialize(answers: List<ChooseOneAnswer>): Unit {

        val items = answers.map {

            ChooseOneAnswerItem(
                it.id,
                it.text,
                false,
                it.textColor,
                it.buttonColor
            )
        }

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