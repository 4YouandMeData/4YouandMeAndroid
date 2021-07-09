package com.foryouandme.researchkit.step.date

import androidx.compose.ui.graphics.Color
import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.ui.compose.textfield.EntryDateColors
import org.threeten.bp.LocalDate

class DatePickerStep(
    identifier: String,
    back: Back,
    skip: Skip,
    val backgroundColor: ColorSource,
    val image: ImageSource?,
    val questionId: String,
    val question: TextSource,
    val questionColor: ColorSource,
    val shadowColor: ColorSource,
    val buttonImage: ImageSource,
    val entryDateColors: EntryDateColors,
    val minDate: LocalDate?,
    val maxDate: LocalDate?
) : Step(identifier, back, skip, { DatePickerStepFragment() })