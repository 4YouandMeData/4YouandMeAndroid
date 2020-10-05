package org.fouryouandme.researchkit.step.choosemany

import android.content.Context
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class ChooseManyStep(
    identifier: String,
    val values: List<ChooseManyAnswer>,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource
) : Step(identifier, { ChooseManyStepFragment() })