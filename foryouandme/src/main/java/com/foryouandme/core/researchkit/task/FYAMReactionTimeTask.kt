package com.foryouandme.core.researchkit.task

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.reaction.ReactionTimeTask

class FYAMReactionTimeTask(
    id: String,
    reactionTimeTitle: String?,
    reactionTimeMaximumStimulusIntervalSeconds: Double,
    reactionTimeMinimumStimulusIntervalSeconds: Double,
    reactionTimeThresholdAcceleration: Double,
    reactionTimeNumberOfAttempts: Int,
    reactionTimeTimeoutSeconds: Double,
    private val configuration: Configuration
) : Task(TaskIdentifiers.TRAIL_MAKING, id) {

    override val steps: List<Step> by lazy {

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val secondary =
            configuration.theme.secondaryColor.color()

        ReactionTimeTask.getReactionTimeCoreSteps(
            reactionTimeTitle,
            primaryText,
            secondary,
            reactionTimeMaximumStimulusIntervalSeconds,
            reactionTimeMinimumStimulusIntervalSeconds,
            reactionTimeThresholdAcceleration,
            reactionTimeNumberOfAttempts,
            reactionTimeTimeoutSeconds
        )

    }

}