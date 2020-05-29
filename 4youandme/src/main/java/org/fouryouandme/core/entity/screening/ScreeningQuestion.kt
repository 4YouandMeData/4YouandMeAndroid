package org.fouryouandme.core.entity.screening

data class ScreeningQuestion(
    val text: String,
    val answers1: ScreeningAnswer,
    val answers2: ScreeningAnswer
)