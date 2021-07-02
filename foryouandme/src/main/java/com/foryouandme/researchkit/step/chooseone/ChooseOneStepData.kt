package com.foryouandme.researchkit.step.chooseone

import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.chooseone.compose.ChooseOneAnswerData
import org.threeten.bp.ZonedDateTime

data class ChooseOneState(
    val step: ChooseOneStep? = null,
    val answers: List<ChooseOneAnswerData> = emptyList(),
    val canGoNext: Boolean = false,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class ChooseOneAction {

    data class SetStep(val step: ChooseOneStep) : ChooseOneAction()
    data class Answer(val answerId: String) : ChooseOneAction()
    data class AnswerTextChange(val answerId: String, val text: String) : ChooseOneAction()
    object Next : ChooseOneAction()

}

sealed class ChooseOneEvent {

    data class Skip(
        val result: SingleAnswerResult?,
        val target: SkipTarget
    ) : ChooseOneEvent()

    data class Next(val result: SingleAnswerResult?) : ChooseOneEvent()

}

