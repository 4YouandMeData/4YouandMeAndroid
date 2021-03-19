package com.foryouandme.entity.survey

data class SurveyAnswer(
    val id: String,
    val text: String,
    val correct: Boolean,
    val isNone: Boolean
)