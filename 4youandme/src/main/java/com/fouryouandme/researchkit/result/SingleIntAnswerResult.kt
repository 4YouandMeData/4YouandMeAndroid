package com.fouryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class SingleIntAnswerResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val questionId: String,
    val answer: Int
) : StepResult(identifier, startDate, endDate)