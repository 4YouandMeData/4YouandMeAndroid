package org.fouryouandme.researchkit.step

import org.fouryouandme.core.entity.configuration.Configuration

sealed class Step(val identifier: String, val configuration: Configuration) {

    class IntroductionStep(
        identifier: String,
        configuration: Configuration,
        val title: String,
        val description: String,
        val image: Int,
        val button: String
    ) : Step(identifier, configuration)

    class CountDownStep(
        identifier: String,
        configuration: Configuration,
        val title: String,
        val seconds: Int
    ) : Step(identifier, configuration)

}