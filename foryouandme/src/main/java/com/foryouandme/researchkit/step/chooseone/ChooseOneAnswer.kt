package com.foryouandme.researchkit.step.chooseone

import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.TextSource

data class ChooseOneAnswer(
    val id: String,
    val text: TextSource,
    val textColor: ColorSource,
    val selectedColor: ColorSource,
    val unselectedColor: ColorSource,
    val isOther: Boolean,
    val otherPlaceholder: TextSource?
)