package org.fouryouandme.researchkit.step.range

import android.content.Context
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class RangeStep(
    identifier: String,
    val minValue: Int,
    val maxValue: Int,
    val progressColor: Int,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource
) : Step(identifier, { RangeStepFragment() })