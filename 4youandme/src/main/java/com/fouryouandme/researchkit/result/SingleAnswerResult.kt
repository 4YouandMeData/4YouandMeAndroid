package com.fouryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class SingleAnswerResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val questionId: String,
    val answer: String
) :
    StepResult(identifier, startDate, endDate)