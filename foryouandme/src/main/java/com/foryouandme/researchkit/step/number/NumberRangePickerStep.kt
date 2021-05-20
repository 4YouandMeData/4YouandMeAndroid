package com.foryouandme.researchkit.step.number

import com.foryouandme.researchkit.skip.SurveySkip
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.TextSource

class NumberRangePickerStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val min: Int,
    val max: Int,
    val minDisplayValue: String?,
    val maxDisplayValue: String?,
    val backgroundColor: Int,
    val image: ImageSource?,
    val questionId: String,
    val question: TextSource,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageSource,
    val arrowColor: Int,
    val skips: List<SurveySkip.Range>
) : Step(identifier, back, skip, { NumberRangePickerStepFragment() })