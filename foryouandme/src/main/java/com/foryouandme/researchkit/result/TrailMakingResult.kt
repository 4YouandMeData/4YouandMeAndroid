package com.foryouandme.researchkit.result

import com.foryouandme.entity.task.trailmaking.TrailMakingTap
import org.threeten.bp.ZonedDateTime

class TrailMakingResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val numberOfErrors: Int,
    val taps: List<TrailMakingTap>
) : StepResult(identifier, startDate, endDate)