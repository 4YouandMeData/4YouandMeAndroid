package com.fouryouandme.researchkit.step.scale

import android.content.Context
import com.fouryouandme.researchkit.skip.SurveySkip
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Skip
import com.fouryouandme.researchkit.step.Step
import com.fouryouandme.researchkit.utils.ImageResource

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