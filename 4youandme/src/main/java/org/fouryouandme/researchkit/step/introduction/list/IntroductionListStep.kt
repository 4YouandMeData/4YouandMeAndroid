package org.fouryouandme.researchkit.step.introduction.list

import android.content.Context
import org.fouryouandme.researchkit.step.Step

class IntroductionListStep(
    identifier: String,
    backImage: Int,
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
    val toolbarColor: Int
) : Step(identifier, backImage, { IntroductionListStepFragment() })