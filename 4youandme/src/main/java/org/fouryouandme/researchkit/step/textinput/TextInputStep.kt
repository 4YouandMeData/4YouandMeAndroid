package org.fouryouandme.researchkit.step.textinput

import android.content.Context
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.utils.ImageResource

class TextInputStep(
    identifier: String,
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
) : Step(identifier, { TextInputStepFragment() })