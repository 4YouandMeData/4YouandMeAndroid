package com.foryouandme.researchkit.step.introduction

import com.foryouandme.entity.resources.TextResource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step

class IntroductionStep(
    identifier: String,
    back: Back,
    val backgroundColor: Int,
    val title: TextResource,
    val titleColor: Int,
    val description: TextResource,
    val descriptionColor: Int,
    val image: Int,
    val button: TextResource,
    val buttonColor: Int,
    val buttonTextColor: Int,
) : Step(identifier, back, null, { IntroductionStepFragment() })