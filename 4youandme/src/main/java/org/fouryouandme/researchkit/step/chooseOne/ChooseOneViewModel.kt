package org.fouryouandme.researchkit.step.chooseOne

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class ChooseOneViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, ChooseOneStepState, ChooseOneStepStateUpdate, Empty, Empty>(
        navigator = navigator,
        runtime = runtime
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

        setStateFx(ChooseOneStepState(items)) { ChooseOneStepStateUpdate.Initialization(it.items) }

    }

    suspend fun answer(answerId: String): Unit {

        val items = state().items.map {
            if (it is ChooseOneAnswerItem) it.copy(isSelected = it.id == answerId)
            else it
        }

        setStateFx(state().copy(items = items)) { ChooseOneStepStateUpdate.Answer(it.items) }

    }

    fun getSelectedAnswer(): ChooseOneAnswerItem? =
        state().items.firstOrNull {
            when (it) {
                is ChooseOneAnswerItem -> it.isSelected
                else -> false
            }
        } as? ChooseOneAnswerItem
}