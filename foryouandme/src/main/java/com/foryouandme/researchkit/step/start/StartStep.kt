package com.foryouandme.researchkit.step.start

import android.content.Context
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Step

class StartStep(
    identifier: String,
    block: Block,
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
) : Step(
    identifier = identifier,
    back = back,
    block = block,
    skip = null,
    view = { StartStepFragment() }
)