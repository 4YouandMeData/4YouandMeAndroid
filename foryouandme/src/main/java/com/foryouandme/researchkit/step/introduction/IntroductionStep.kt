package com.foryouandme.researchkit.step.introduction

import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step

class IntroductionStep(
    identifier: String,
    back: Back,
    val backgroundColor: Int,
    val title: TextSource,
    val titleColor: Int,
    val description: TextSource,
    val descriptionColor: Int,
    val image: List<Int>,
    val button: TextSource,
    val buttonColor: Int,
    val buttonTextColor: Int,
) : Step(identifier, back, null, { IntroductionStepFragment() })