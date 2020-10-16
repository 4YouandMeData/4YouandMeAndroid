package org.fouryouandme.researchkit.step.chooseone

import android.content.Context
import org.fouryouandme.researchkit.skip.SurveySkip
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class ChooseOneStep(
    identifier: String,
    val values: List<ChooseOneAnswer>,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val skips: List<SurveySkip.Answer>
) : Step(identifier, { ChooseOneStepFragment() })