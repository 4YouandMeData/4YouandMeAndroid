package org.fouryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class MultipleAnswerResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val questionId: String,
    val answer: List<String>
) :
    StepResult(identifier, startDate, endDate) {
}