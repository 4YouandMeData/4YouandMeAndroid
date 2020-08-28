package org.fouryouandme.researchkit.task

import arrow.core.Option
import arrow.core.some
import arrow.core.toOption
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.recorder.config.AccelerometerRecorderConfig
import org.fouryouandme.researchkit.recorder.config.DeviceMotionRecorderConfig
import org.fouryouandme.researchkit.recorder.config.PedometerRecorderConfig
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
            imageConfiguration: ImageConfiguration,
            moshi: Moshi
        ): Option<Task> =
            when (identifier) {
                "reaction_time" -> ReactionTimeTask(
                    configuration,
                    imageConfiguration,
                    moshi
                )
                else -> null
            }.toOption()

    }

    class ReactionTimeTask(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        private val moshi: Moshi
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
                Step.ActiveStep(
                    "active_step",
                    configuration,
                    "Active Step",
                    "Cammina avanti e indietro per 10 secondi",
                    10,
                    listOf(
                        PedometerRecorderConfig(moshi),
                        AccelerometerRecorderConfig(moshi, 100.toDouble()),
                        DeviceMotionRecorderConfig(moshi, 100.toDouble())
                    ),
                    "Cammina avanti e indietro per 10 secondi".some()
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