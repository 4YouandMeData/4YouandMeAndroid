package com.foryouandme.researchkit.step.choosemany

import android.content.Context
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.entity.source.ImageSource

class ChooseManyStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val values: List<ChooseManyAnswer>,
    val backgroundColor: Int,
    val image: ImageSource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageSource,
    val skips: List<SurveySkip.Answer>
) : Step(identifier, back, skip, { ChooseManyStepFragment() })