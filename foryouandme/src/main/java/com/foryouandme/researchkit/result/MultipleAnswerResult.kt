package com.foryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class MultipleAnswerResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val questionId: String,
    val answers: List<String>
) :
    StepResult(identifier, startDate, endDate) {
}