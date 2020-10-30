package com.fouryouandme.core.entity.consent.informed

import arrow.core.Nel

data class ConsentInfoQuestion(
    val id: String,
    val text: String,
    val answers: Nel<ConsentInfoAnswer>
)