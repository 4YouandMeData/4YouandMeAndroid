package org.fouryouandme.researchkit.step.date

import android.content.Context
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class DatePickerStep(
    identifier: String,
    backImage: Int,
    canSkip: Boolean,
    val backgroundColor: Int,
    val image: ImageResource?,
    val questionId: String,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: ImageResource,
    val minDate: Long?,
    val maxDate: Long?
) : Step(identifier, backImage, canSkip, { DatePickerStepFragment() })