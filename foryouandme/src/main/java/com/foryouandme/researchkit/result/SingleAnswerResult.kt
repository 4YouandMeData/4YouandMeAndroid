package com.foryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class SingleAnswerResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val questionId: String,
    val answer: AnswerResult
) : StepResult(identifier, startDate, endDate)