package com.foryouandme.entity.consent.informed

data class ConsentInfoQuestion(
    val id: String,
    val text: String,
    val answers: List<ConsentInfoAnswer>
)