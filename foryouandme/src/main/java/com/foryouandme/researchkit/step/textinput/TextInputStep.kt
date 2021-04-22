package com.foryouandme.researchkit.step.textinput

import android.content.Context
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Skip
import com.foryouandme.researchkit.step.Step
import com.foryouandme.entity.resources.ImageResource

class TextInputStep(
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
    val textColor: Int,
    val placeholderColor: Int,
    val placeholder: String?,
    val maxCharacters: Int?
) : Step(identifier, back, skip, { TextInputStepFragment() })