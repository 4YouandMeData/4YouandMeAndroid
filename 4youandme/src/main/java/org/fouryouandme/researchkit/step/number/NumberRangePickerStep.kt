package org.fouryouandme.researchkit.step.number

import android.content.Context
import org.fouryouandme.researchkit.skip.SurveySkip
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class NumberRangePickerStep(
    identifier: String,
    backImage: Int,
    canSkip: Boolean,
    val min: Int,
    val max: Int,
    val minDisplayValue: String?,
    val maxDisplayValue: String?,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val skips: List<SurveySkip.Range>
) : Step(identifier, backImage, canSkip, { NumberRangePickerStepFragment() })