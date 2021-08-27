package com.foryouandme.researchkit.step.chooseone

import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step

class ChooseOneStep(
    identifier: String,
    block: Block,
    back: Back,
    skip: Skip,
    val values: List<ChooseOneAnswer>,
    val backgroundColor: ColorSource,
    val image: ImageSource?,
    val questionId: String,
    val question: TextSource,
    val questionColor: ColorSource,
    val buttonImage: ImageSource,
    val skips: List<SurveySkip.Answer>
) : Step(
    identifier = identifier,
    block = block,
    back = back,
    skip = skip,
    view = { ChooseOneStepFragment() }
)