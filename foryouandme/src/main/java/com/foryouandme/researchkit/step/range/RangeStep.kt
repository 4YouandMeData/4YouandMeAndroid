package com.foryouandme.researchkit.step.range

import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step

class RangeStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val minValue: Int,
    val maxValue: Int,
    val valueColor: ColorSource,
    val minDisplayValue: TextSource?,
    val maxDisplayValue: TextSource?,
    val minDisplayColor: ColorSource,
    val maxDisplayColor: ColorSource,
    val progressColor: ColorSource,
    val backgroundColor: ColorSource,
    val image: ImageSource?,
    val questionId: String,
    val question: TextSource,
    val questionColor: ColorSource,
    val shadowColor: ColorSource,
    val buttonImage: ImageSource,
    val skips: List<SurveySkip.Range>
) : Step(identifier, back, skip, { RangeStepFragment() })