package org.fouryouandme.tasks.step

import org.fouryouandme.core.entity.configuration.Configuration

sealed class Step(val configuration: Configuration) {

    class IntroductionStep(
        configuration: Configuration,
        val title: String
    ) : Step(configuration)

}