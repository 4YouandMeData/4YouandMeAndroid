package com.foryouandme.researchkit.step.chooseone

import com.giacomoparisi.recyclerdroid.core.DroidItem

data class ChooseOneStepState(val items: List<DroidItem<Any>> = emptyList())

sealed class ChooseOneStepStateUpdate {
    data class Initialization(val items: List<DroidItem<Any>>) : ChooseOneStepStateUpdate()
    data class Answer(val items: List<DroidItem<Any>>) : ChooseOneStepStateUpdate()
}

sealed class ChooseOneStepStateEvent {

    data class Initialize(
        val step: ChooseOneStep,
        val answers: List<ChooseOneAnswer>
    ) : ChooseOneStepStateEvent()

    data class Answer(val answerId: String) : ChooseOneStepStateEvent()
    data class AnswerTextChange(val answerId: String, val text: String) : ChooseOneStepStateEvent()

}

