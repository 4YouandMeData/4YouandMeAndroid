package org.fouryouandme.researchkit.step.scale

import android.content.Context
import org.fouryouandme.researchkit.skip.SurveySkip
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class ScaleStep(
    identifier: String,
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
) : Step(identifier, { ScaleStepFragment() })