package com.foryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class SingleStringAnswerResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val questionId: String,
    val answer: String
) :
    StepResult(identifier, startDate, endDate)