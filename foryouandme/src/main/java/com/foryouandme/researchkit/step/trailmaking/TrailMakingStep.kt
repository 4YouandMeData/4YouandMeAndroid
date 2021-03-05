package com.foryouandme.researchkit.step.trailmaking

import com.foryouandme.researchkit.step.Step

class TrailMakingStep(
    identifier: String,
    val backgroundColor: Int,
) : Step(identifier, null, null, { TrailMakingStepFragment() })