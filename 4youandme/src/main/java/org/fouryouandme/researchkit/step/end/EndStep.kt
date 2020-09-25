package org.fouryouandme.researchkit.step.end

import org.fouryouandme.researchkit.step.Step

class EndStep(
    identifier: String,
    val backgroundColor: Int,
    val title: String,
    val titleColor: Int,
    val description: String,
    val descriptionColor: Int,
    val button: String,
    val buttonColor: Int,
    val buttonTextColor: Int,
    val close: Boolean = false,
    val checkMarkBackgroundColor: Int,
    val checkMarkColor: Int
) : Step(identifier, { EndStepFragment() })