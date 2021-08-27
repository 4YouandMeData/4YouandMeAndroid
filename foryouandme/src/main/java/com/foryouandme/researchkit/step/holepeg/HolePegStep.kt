package com.foryouandme.researchkit.step.holepeg

import com.foryouandme.entity.task.holepeg.HolePegSubStep
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Step

class HolePegStep(
    identifier: String,
    block: Block,
    val backgroundColor: Int,
    val title: String? = null,
    val titleColor: Int,
    val descriptionStartCenter: String? = null,
    val descriptionEndCenter: String? = null,
    val descriptionStart: String? = null,
    val descriptionEnd: String? = null,
    val descriptionGrab: String? = null,
    val descriptionRelease: String? = null,
    val descriptionColor: Int,
    val progressColor: Int,
    val pointColor: Int,
    val targetColor: Int,
    val subSteps: List<HolePegSubStep>,
    val numberOfPegs: Int = 9
) : Step(
    identifier = identifier,
    back = null,
    block = block,
    skip = null,
    view = { HolePegFragment() }
)