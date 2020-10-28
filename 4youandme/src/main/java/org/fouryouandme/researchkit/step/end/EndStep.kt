package org.fouryouandme.researchkit.step.end

import android.content.Context
import org.fouryouandme.researchkit.step.Step

class EndStep(
    identifier: String,
    val backgroundColor: Int,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val button: (Context) -> String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val close: Boolean = false,
    val checkMarkBackgroundColor: Int,
    val checkMarkColor: Int
) : Step(identifier, null, null, { EndStepFragment() })