package org.fouryouandme.researchkit.task

import arrow.core.Option
import arrow.core.toOption
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.step.Step

sealed class Task(
    val identifier: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration
) {

    val steps: List<Step> by lazy { buildSteps(configuration, imageConfiguration) }

    protected open fun buildSteps(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ): List<Step> = emptyList()

    companion object {

        fun byIdentifier(
            identifier: String,
            configuration: Configuration,
            imageConfiguration: ImageConfiguration
        ): Option<Task> =
            when (identifier) {
                "reaction_time" -> ReactionTimeTask(
                    configuration,
                    imageConfiguration
                )
                else -> null
            }.toOption()

    }

    class ReactionTimeTask(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ) : Task("reaction_time", configuration, imageConfiguration) {

        override fun buildSteps(
            configuration: Configuration,
            imageConfiguration: ImageConfiguration
        ): List<Step> =
            listOf( // TODO: Remove mock
                Step.IntroductionStep(
                    "introduction_1",
                    configuration,
                    "Reaction Time Task",
                    "description",
                    imageConfiguration.close(),
                    "Next"
                ),
                Step.CountDownStep(
                    "countdown_1",
                    configuration,
                    "Reaction Time Task",
                    10
                ),
                Step.IntroductionStep(
                    "introduction_2",
                    configuration,
                    "Reaction Time Task 2",
                    "description",
                    imageConfiguration.close(),
                    "Next"
                ),
                Step.IntroductionStep(
                    "introduction_3",
                    configuration,
                    "Reaction Time Task 3",
                    "description",
                    imageConfiguration.close(),
                    "Next"
                )
            )

    }

}