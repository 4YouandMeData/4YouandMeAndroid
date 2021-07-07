package com.foryouandme.researchkit.step.choosemany

import com.foryouandme.researchkit.result.MultipleAnswerResult
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.choosemany.compose.ChooseManyAnswerData
import org.threeten.bp.ZonedDateTime

data class ChooseManyState(
    val step: ChooseManyStep? = null,
    val answers: List<ChooseManyAnswerData> = emptyList(),
    val canGoNext: Boolean = false,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class ChooseManyAction {

    data class SetStep(val step: ChooseManyStep) : ChooseManyAction()
    data class Answer(val id: String) : ChooseManyAction()
    data class AnswerTextChange(val answerId: String, val text: String) : ChooseManyAction()
    object Next : ChooseManyAction()

}

sealed class ChooseManyEvent {

    data class Skip(
        val result: MultipleAnswerResult?,
        val target: SkipTarget
    ) : ChooseManyEvent()

    data class Next(val result: MultipleAnswerResult?) : ChooseManyEvent()

}

