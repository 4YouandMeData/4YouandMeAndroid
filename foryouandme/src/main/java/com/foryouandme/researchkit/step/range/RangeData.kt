package com.foryouandme.researchkit.step.range

import com.foryouandme.researchkit.result.SingleIntAnswerResult
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import org.threeten.bp.ZonedDateTime

data class RangeState(
    val step: RangeStep? = null,
    val value: Int = 0,
    val valuePercent: Float = 0f,
    val canGoNext: Boolean = false,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class RangeAction {

    data class SetStep(val step: RangeStep) : RangeAction()
    data class SelectValue(val value: Float) : RangeAction()
    object EndValueSelection : RangeAction()
    object Next : RangeAction()

}

sealed class RangeEvents {

    data class Skip(
        val result: SingleIntAnswerResult?,
        val target: SkipTarget
    ) : RangeEvents()

    data class Next(val result: SingleIntAnswerResult?) : RangeEvents()

}