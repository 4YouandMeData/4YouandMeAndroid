package org.fouryouandme.tasks.step

import org.fouryouandme.core.entity.configuration.Configuration

sealed class Step(val configuration: Configuration) {

    class IntroductionStep(
        configuration: Configuration,
        val title: String,
        val description: String,
        val image: Int,
        val button: String
    ) : Step(configuration)

}