package com.foryouandme.researchkit.step.choosemany

import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.TextSource

data class ChooseManyAnswer(
    val id: String,
    val text: TextSource,
    val textColor: ColorSource,
    val entryColor: ColorSource,
    val selectedColor: ColorSource,
    val unselectedColor: ColorSource,
    val checkmarkColor: ColorSource,
    val isNone: Boolean,
    val isOther: Boolean,
    val otherPlaceholder: TextSource?
)