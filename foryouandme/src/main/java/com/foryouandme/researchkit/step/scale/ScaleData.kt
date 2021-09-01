package com.foryouandme.researchkit.step.scale

import com.foryouandme.researchkit.result.SingleIntAnswerResult
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import org.threeten.bp.ZonedDateTime

data class ScaleState(
    val step: ScaleStep? = null,
    val valueDisplay: Int = 0,
    val value: Int = 0,
    val valuePercent: Float = 0f,
    val canGoNext: Boolean = false,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class ScaleAction {

    data class SetStep(val step: ScaleStep) : ScaleAction()
    data class SelectValue(val value: Float) : ScaleAction()
    object EndValueSelection : ScaleAction()
    object Next : ScaleAction()

}

sealed class ScaleEvents {

    data class Skip(
        val result: SingleIntAnswerResult?,
        val target: SkipTarget
    ) : ScaleEvents()

    data class Next(val result: SingleIntAnswerResult?) : ScaleEvents()

}