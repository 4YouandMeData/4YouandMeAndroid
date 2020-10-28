package org.fouryouandme.researchkit.step.range

import android.content.Context
import org.fouryouandme.researchkit.skip.SurveySkip
import org.fouryouandme.researchkit.step.Back
import org.fouryouandme.researchkit.step.Skip
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class RangeStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val minValue: Int,
    val maxValue: Int,
    val valueColor: Int,
    val minDisplayValue: String?,
    val maxDisplayValue: String?,
    val minDisplayColor: Int,
    val maxDisplayColor: Int,
    val progressColor: Int,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val skips: List<SurveySkip.Range>
) : Step(identifier, back, skip, { RangeStepFragment() })