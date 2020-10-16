package org.fouryouandme.researchkit.step.choosemany

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class ChooseManyViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, ChooseManyStepState, ChooseManyStepStateUpdate, Empty, Empty>(
        navigator = navigator,
        runtime = runtime
    ) {

    suspend fun initialize(answers: List<ChooseManyAnswer>): Unit {

        val items = answers.map {

            ChooseManyAnswerItem(
                it.id,
                it.text,
                false,
                it.textColor,
                it.buttonColor
            )
        }

        setStateFx(ChooseManyStepState(items)) { ChooseManyStepStateUpdate.Initialization(it.items) }

    }

    suspend fun answer(answerId: String): Unit {

        val items = state().items.map {
            if (it is ChooseManyAnswerItem) {
                if (it.id == answerId) it.copy(isSelected = it.isSelected.not())
                else it
            } else it
        }

        setStateFx(state().copy(items = items)) { ChooseManyStepStateUpdate.Answer(it.items) }

    }

    fun getSelectedAnswers(): List<ChooseManyAnswerItem> =
        state().items
            .mapNotNull { it as? ChooseManyAnswerItem }
            .filter { it.isSelected }
}