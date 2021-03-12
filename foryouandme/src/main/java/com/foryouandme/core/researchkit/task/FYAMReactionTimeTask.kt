package com.foryouandme.core.researchkit.task

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.reaction.ReactionTimeTask

class FYAMReactionTimeTask(
    id: String,
    reactionTimeTitle: String?,
    reactionTimeMaximumStimulusIntervalSeconds: Long,
    reactionTimeMinimumStimulusIntervalSeconds: Long,
    reactionTimeNumberOfAttempts: Int,
    reactionTimeTimeoutSeconds: Long,
    private val configuration: Configuration
) : Task(TaskIdentifiers.TRAIL_MAKING, id) {

    override val steps: List<Step> by lazy {

        val primaryEnd =
            configuration.theme.primaryColorEnd.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val secondary =
            configuration.theme.secondaryColor.color()

        ReactionTimeTask.getReactionTimeCoreSteps(
            reactionTimeBackgroundColor = secondary,
            reactionTimeTitle = reactionTimeTitle,
            reactionTimeTitleColor = primaryText,
            reactionTimeAttemptColor = primaryText,
            reactionCheckMarkBackgroundColor = primaryEnd,
            reactionCheckMarkColor = secondary,
            reactionTimeMaximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
            reactionTimeMinimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
            reactionTimeNumberOfAttempts = reactionTimeNumberOfAttempts,
            reactionTimeTimeoutSeconds = reactionTimeTimeoutSeconds
        )

    }

}