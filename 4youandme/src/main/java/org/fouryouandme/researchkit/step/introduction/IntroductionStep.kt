package org.fouryouandme.researchkit.step.introduction

import org.fouryouandme.researchkit.step.Step

class IntroductionStep(
    identifier: String,
    val backgroundColor: Int,
    val title: String,
    val titleColor: Int,
    val description: String,
    val descriptionColor: Int,
    val image: Int,
    val button: String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val close: Boolean = false
) : Step(identifier, { IntroductionStepFragment() })