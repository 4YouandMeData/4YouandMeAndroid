package com.foryouandme.entity.task.result.trailmaking

import com.foryouandme.researchkit.result.StepResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.trailmaking.TrailMakingTask
import org.threeten.bp.ZonedDateTime

data class Point(val x: Double, val y: Double)
data class TrailMakingPoint(val x: Double, val y: Double, val name: String)
data class TrailMakingTap(val index: Int, val timestamp: Long, val incorrect: Boolean)

class TrailMakingResult(
    identifier: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    val numberOfErrors: Int,
    val taps: List<TrailMakingTap>
) : StepResult(identifier, startDate, endDate)

fun TaskResult.toTrailMakingResult(): TrailMakingResult? =
    results[TrailMakingTask.TRAIL_MAKING] as? TrailMakingResult
