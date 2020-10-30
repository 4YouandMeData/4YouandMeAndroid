package com.fouryouandme.researchkit.step.chooseone

import android.content.Context
import com.fouryouandme.researchkit.skip.SurveySkip
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Skip
import com.fouryouandme.researchkit.step.Step
import com.fouryouandme.researchkit.utils.ImageResource

class ChooseOneStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val values: List<ChooseOneAnswer>,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val skips: List<SurveySkip.Answer>
) : Step(identifier, back, skip, { ChooseOneStepFragment() })