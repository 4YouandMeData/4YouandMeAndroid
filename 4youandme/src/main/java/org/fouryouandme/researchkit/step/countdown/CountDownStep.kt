package org.fouryouandme.researchkit.step.countdown

import org.fouryouandme.researchkit.step.Step

class CountDownStep(
    identifier: String,
    val backgroundColor: Int,
    val title: String,
    val titleColor: Int,
    val description: String,
    val descriptionColor: Int,
    val seconds: Int,
    val counterColor: Int,
    val counterProgressColor: Int
) : Step(identifier, { CountDownStepFragment() })