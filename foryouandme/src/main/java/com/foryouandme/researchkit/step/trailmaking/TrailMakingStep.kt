package com.foryouandme.researchkit.step.trailmaking

import com.foryouandme.researchkit.step.Step

class TrailMakingStep(
    identifier: String,
    val type: ETrailMakingType,
    val backgroundColor: Int,
    val pointColor: Int,
    val pointTextColor: Int,
    val lineColor: Int,
) : Step(identifier, null, null, { TrailMakingStepFragment() })

enum class ETrailMakingType {

    NUMBER,
    NUMBER_AND_LETTER

}