package com.foryouandme.researchkit.step.textinput

import com.foryouandme.researchkit.result.SingleStringAnswerResult
import org.threeten.bp.ZonedDateTime

data class TextInputState(
    val step: TextInputStep? = null,
    val text: String = "",
    val canGoNext: Boolean = false,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class TextInputAction {

    data class SetStep(val step: TextInputStep) : TextInputAction()
    data class SetText(val text: String) : TextInputAction()
    object Next : TextInputAction()

}

sealed class TextInputEvent {

    data class Next(val result: SingleStringAnswerResult?) : TextInputEvent()

}

