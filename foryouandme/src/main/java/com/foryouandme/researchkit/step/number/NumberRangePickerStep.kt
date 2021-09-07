package com.foryouandme.researchkit.step.number

import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step

class NumberRangePickerStep(
    identifier: String,
    block: Block,
    back: Back,
    skip: Skip,
    val min: Int,
    val max: Int,
    val minDisplayValue: String?,
    val maxDisplayValue: String?,
    val backgroundColor: ColorSource,
    val image: ImageSource?,
    val questionId: String,
    val question: TextSource,
    val questionColor: ColorSource,
    val buttonImage: ImageSource,
    val arrowColor: ColorSource,
    val skips: List<SurveySkip.Range>
) : Step(
    identifier = identifier,
    block = block,
    back = back,
    skip = skip,
    view = { NumberRangePickerStepFragment() }
)