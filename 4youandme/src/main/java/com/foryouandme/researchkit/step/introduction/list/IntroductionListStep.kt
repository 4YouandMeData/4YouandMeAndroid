package com.foryouandme.researchkit.step.introduction.list

import android.content.Context
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step

class IntroductionListStep(
    identifier: String,
    back: Back,
    val backgroundColor: Int,
    val footerBackgroundColor: Int,
    val title: String,
    val titleColor: Int,
    val image: Int,
    val remindButton: (Context) -> String,
    val remindButtonColor: Int,
    val remindButtonTextColor: Int,
    val button: String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val list: List<IntroductionItem>,
    val shadowColor: Int,
) : Step(identifier, back, null, { IntroductionListStepFragment() })