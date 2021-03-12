package com.foryouandme.researchkit.result

import org.threeten.bp.ZonedDateTime

class JsonResult(
    identifier: String,
    val json: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime
) : StepResult(identifier, startDate, endDate)