package org.fouryouandme.researchkit.step.beforestart

import android.content.Context
import org.fouryouandme.researchkit.step.Step

class WelcomeStep(
    identifier: String,
    backImage: Int,
    val backgroundColor: Int,
    val image: Int?,
    val title: (Context) -> String,
    val titleColor: Int,
    val description: (Context) -> String,
    val descriptionColor: Int,
    val remindButton: (Context) -> String,
    val remindButtonColor: Int,
    val remindButtonTextColor: Int,
    val startButton: (Context) -> String,
    val startButtonColor: Int,
    val startButtonTextColor: Int,
    val shadowColor: Int,
    val close: Boolean = false
) : Step(identifier, backImage, { WelcomeStepFragment() })