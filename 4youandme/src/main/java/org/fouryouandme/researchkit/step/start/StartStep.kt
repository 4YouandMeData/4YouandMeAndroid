package org.fouryouandme.researchkit.step.start

import android.content.Context
import org.fouryouandme.researchkit.step.Step

class StartStep(
    identifier: String,
    backImage: Int,
    val backgroundColor: Int,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val button: (Context) -> String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val close: Boolean = false
) : Step(identifier, backImage, false, { StartStepFragment() })