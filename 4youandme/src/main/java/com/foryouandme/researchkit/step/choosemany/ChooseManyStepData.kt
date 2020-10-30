package com.foryouandme.researchkit.step.choosemany

import com.giacomoparisi.recyclerdroid.core.DroidItem

data class ChooseManyStepState(val items: List<DroidItem<Any>>)

sealed class ChooseManyStepStateUpdate {
    data class Initialization(val items: List<DroidItem<Any>>) : ChooseManyStepStateUpdate()
    data class Answer(val items: List<DroidItem<Any>>) : ChooseManyStepStateUpdate()
}

