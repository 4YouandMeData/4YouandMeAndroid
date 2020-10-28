package org.fouryouandme.researchkit.step.start

import android.content.Context
import org.fouryouandme.researchkit.step.Back
import org.fouryouandme.researchkit.step.Step

class StartStep(
    identifier: String,
    back: Back,
    val backgroundColor: Int,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val button: (Context) -> String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val close: Boolean = false
) : Step(identifier, back, null, { StartStepFragment() })