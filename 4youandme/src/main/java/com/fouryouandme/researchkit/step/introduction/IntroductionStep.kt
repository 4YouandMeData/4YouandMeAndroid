package com.fouryouandme.researchkit.step.introduction

import android.content.Context
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Step

class IntroductionStep(
    identifier: String,
    back: Back,
    val backgroundColor: Int,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val image: Int,
    val button: (Context) -> String,
    val buttonColor: Int,
    val buttonTextColor: Int,
) : Step(identifier, back, null, { IntroductionStepFragment() })