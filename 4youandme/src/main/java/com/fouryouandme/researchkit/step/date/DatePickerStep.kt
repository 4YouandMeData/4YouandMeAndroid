package com.fouryouandme.researchkit.step.date

import android.content.Context
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Skip
import com.fouryouandme.researchkit.step.Step
import com.fouryouandme.researchkit.utils.ImageResource

class DatePickerStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val minDate: Long?,
    val maxDate: Long?
) : Step(identifier, back, skip, { DatePickerStepFragment() })