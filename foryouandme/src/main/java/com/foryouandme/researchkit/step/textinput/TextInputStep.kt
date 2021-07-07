package com.foryouandme.researchkit.step.textinput

import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step

class TextInputStep(
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
    val textColor: ColorSource,
    val placeholderColor: ColorSource,
    val placeholder: TextSource?,
    val maxCharacters: Int?
) : Step(identifier, back, skip, { TextInputStepFragment() })