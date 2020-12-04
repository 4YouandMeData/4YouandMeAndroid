package com.foryouandme.entity.screening

data class ScreeningQuestion(
    val id: String,
    val text: String,
    val answers1: ScreeningAnswer,
    val answers2: ScreeningAnswer
)