package com.foryouandme.researchkit.task.reaction

import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.reaction.ReactionTimeStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class ReactionTimeTask(
    id: String,
    reactionTimeTitle: String?,
    reactionTimeTitleColor: Int,
    reactionTimeBackgroundColor: Int,
    reactionTimeMaximumStimulusIntervalSeconds: Double,
    reactionTimeMinimumStimulusIntervalSeconds: Double,
    reactionTimeThresholdAcceleration: Double,
    reactionTimeNumberOfAttempts: Int,
    reactionTimeTimeoutSeconds: Double
) : Task(TaskIdentifiers.REACTION_TIME, id) {

    override val steps: List<Step> by lazy {

        getReactionTimeCoreSteps(
            reactionTimeTitle = reactionTimeTitle,
            reactionTimeTitleColor = reactionTimeTitleColor,
            reactionTimeBackgroundColor = reactionTimeBackgroundColor,
            reactionTimeMaximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
            reactionTimeMinimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
            reactionTimeThresholdAcceleration = reactionTimeThresholdAcceleration,
            reactionTimeNumberOfAttempts = reactionTimeNumberOfAttempts,
            reactionTimeTimeoutSeconds = reactionTimeTimeoutSeconds
        )

    }


    companion object {

        const val REACTION_TIME = "reaction_time"

        fun getReactionTimeCoreSteps(
            reactionTimeTitle: String?,
            reactionTimeTitleColor: Int,
            reactionTimeBackgroundColor: Int,
            reactionTimeMaximumStimulusIntervalSeconds: Double,
            reactionTimeMinimumStimulusIntervalSeconds: Double,
            reactionTimeThresholdAcceleration: Double,
            reactionTimeNumberOfAttempts: Int,
            reactionTimeTimeoutSeconds: Double
        ): List<Step> =

            listOf(
                ReactionTimeStep(
                    REACTION_TIME,
                    titleText = reactionTimeTitle,
                    titleTextColor = reactionTimeTitleColor,
                    backgroundColor = reactionTimeBackgroundColor,
                    maximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
                    minimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
                    thresholdAcceleration = reactionTimeThresholdAcceleration,
                    numberOfAttempts = reactionTimeNumberOfAttempts,
                    timeoutSeconds = reactionTimeTimeoutSeconds
                )
            )

    }

}