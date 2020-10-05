package org.fouryouandme.researchkit.step.chooseone

import com.giacomoparisi.recyclerdroid.core.DroidItem

data class ChooseOneStepState(val items: List<DroidItem<Any>>)

sealed class ChooseOneStepStateUpdate {
    data class Initialization(val items: List<DroidItem<Any>>) : ChooseOneStepStateUpdate()
    data class Answer(val items: List<DroidItem<Any>>) : ChooseOneStepStateUpdate()
}

