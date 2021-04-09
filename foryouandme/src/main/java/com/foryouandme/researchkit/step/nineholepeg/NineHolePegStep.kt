package com.foryouandme.researchkit.step.nineholepeg

import com.foryouandme.researchkit.step.Step

class NineHolePegStep(
    identifier: String,
    val backgroundColor: Int,
    val title: String? = null,
    val titleColor: Int,
    val descriptionShape: String? = null,
    val descriptionGrab: String? = null,
    val descriptionRelease: String? = null,
    val descriptionColor: Int
) : Step(identifier, null, null, { NineHolePegFragment() })