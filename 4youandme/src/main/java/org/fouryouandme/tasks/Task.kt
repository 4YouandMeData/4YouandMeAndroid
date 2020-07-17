package org.fouryouandme.tasks

import arrow.core.Option
import arrow.core.toOption
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.tasks.step.Step

sealed class Task(val identifier: String, private val configuration: Configuration) {

    val steps: List<Step> by lazy { buildSteps(configuration) }

    protected open fun buildSteps(configuration: Configuration): List<Step> = emptyList()

    companion object {

        fun byIdentifier(identifier: String, configuration: Configuration): Option<Task> =
            when (identifier) {
                "reaction_time" -> ReactionTimeTask(configuration)
                else -> null
            }.toOption()

    }

    class ReactionTimeTask(
        configuration: Configuration
    ) : Task("reaction_time", configuration) {

        override fun buildSteps(configuration: Configuration): List<Step> =
            listOf(
                Step.IntroductionStep(configuration, "PIPPO")
            )

    }

}