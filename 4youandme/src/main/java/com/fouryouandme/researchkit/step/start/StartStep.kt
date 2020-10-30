package com.fouryouandme.researchkit.step.start

import android.content.Context
import com.fouryouandme.researchkit.step.Back
import com.fouryouandme.researchkit.step.Step

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