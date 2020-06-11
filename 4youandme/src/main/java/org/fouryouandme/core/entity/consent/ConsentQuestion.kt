package org.fouryouandme.core.entity.consent

import arrow.core.Nel

data class ConsentQuestion(
    val id: String,
    val text: String,
    val answers: Nel<ConsentAnswer>
)