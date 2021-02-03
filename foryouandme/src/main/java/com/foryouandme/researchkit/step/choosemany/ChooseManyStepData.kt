package com.foryouandme.researchkit.step.choosemany

import com.giacomoparisi.recyclerdroid.core.DroidItem

data class ChooseManyStepState(val items: List<DroidItem<Any>> = emptyList())

sealed class ChooseManyStepStateUpdate {
    data class Initialization(val items: List<DroidItem<Any>>) : ChooseManyStepStateUpdate()
    data class Answer(val items: List<DroidItem<Any>>) : ChooseManyStepStateUpdate()
}

sealed class ChooseManyStepStateEvent {

    data class Initialize(
        val step: ChooseManyStep,
        val answers: List<ChooseManyAnswer>
    ) : ChooseManyStepStateEvent()

    data class Answer(val id: String) : ChooseManyStepStateEvent()

}

