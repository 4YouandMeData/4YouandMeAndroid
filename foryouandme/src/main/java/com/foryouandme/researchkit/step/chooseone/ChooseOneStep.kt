package com.foryouandme.researchkit.step.chooseone

import android.content.Context
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.entity.source.ImageSource

class ChooseOneStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val values: List<ChooseOneAnswer>,
    val backgroundColor: Int,
    val image: ImageSource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageSource,
    val skips: List<SurveySkip.Answer>
) : Step(identifier, back, skip, { ChooseOneStepFragment() })