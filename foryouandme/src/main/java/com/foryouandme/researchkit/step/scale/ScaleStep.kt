package com.foryouandme.researchkit.step.scale

import android.content.Context
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.entity.resources.ImageResource

class ScaleStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val minValue: Int,
    val maxValue: Int,
    val interval: Int,
    val progressColor: Int,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val skips: List<SurveySkip.Range>
) : Step(identifier, back, skip, { ScaleStepFragment() })