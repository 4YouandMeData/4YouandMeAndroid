package org.fouryouandme.researchkit.step.picker

import android.content.Context
import org.fouryouandme.researchkit.step.Step

class PickerStep(
    identifier: String,
    val values: List<String>,
    val backgroundColor: Int,
    val question: (Context) -> String,
    val questionColor: Int,
    val shadowColor: Int,
    val buttonImage: Int
) : Step(identifier, { PickerStepFragment() })