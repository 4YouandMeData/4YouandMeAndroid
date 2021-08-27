package com.foryouandme.researchkit.step.countdown

import android.content.Context
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Step

class CountDownStep(
    identifier: String,
    block: Block,
    back: Back,
    val backgroundColor: Int,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val seconds: Int,
    val counterColor: Int,
    val counterProgressColor: Int
) : Step(
    identifier = identifier,
    back = back,
    block = block,
    skip = null,
    view = { CountDownStepFragment() }
)