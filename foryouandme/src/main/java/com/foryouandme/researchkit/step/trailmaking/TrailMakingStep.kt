package com.foryouandme.researchkit.step.trailmaking

import com.foryouandme.researchkit.step.Step

class TrailMakingStep(
    identifier: String,
    val type: ETrailMakingType,
    val backgroundColor: Int,
    val timerAndErrorTextColor: Int,
    val titleText: String?,
    val titleTextColor: Int,
    val pointColor: Int,
    val pointErrorColor: Int,
    val pointTextColor: Int,
    val lineColor: Int,
) : Step(identifier, null, null, { TrailMakingStepFragment() })

enum class ETrailMakingType {

    NUMBER,
    NUMBER_AND_LETTER

}