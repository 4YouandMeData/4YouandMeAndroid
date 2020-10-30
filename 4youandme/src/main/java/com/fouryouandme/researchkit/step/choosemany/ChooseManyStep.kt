package com.fouryouandme.researchkit.step.choosemany

import android.content.Context
import com.fouryouandme.researchkit.skip.SurveySkip
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Skip
import com.fouryouandme.researchkit.step.Step
import com.fouryouandme.researchkit.utils.ImageResource

class ChooseManyStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val values: List<ChooseManyAnswer>,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val skips: List<SurveySkip.Answer>
) : Step(identifier, back, skip, { ChooseManyStepFragment() })