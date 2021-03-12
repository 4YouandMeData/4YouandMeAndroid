package com.foryouandme.researchkit.task.reaction

import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.reaction.ReactionTimeStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class ReactionTimeTask(
    id: String,
    reactionTimeBackgroundColor: Int,
    reactionTimeTitle: String?,
    reactionTimeTitleColor: Int,
    reactionTimeAttemptColor: Int,
    reactionCheckMarkBackgroundColor: Int,
    reactionCheckMarkColor: Int,
    reactionTimeMaximumStimulusIntervalSeconds: Long,
    reactionTimeMinimumStimulusIntervalSeconds: Long,
    reactionTimeNumberOfAttempts: Int,
    reactionTimeTimeoutSeconds: Long
) : Task(TaskIdentifiers.REACTION_TIME, id) {

    override val steps: List<Step> by lazy {

        getReactionTimeCoreSteps(
            reactionTimeBackgroundColor = reactionTimeBackgroundColor,
            reactionTimeTitle = reactionTimeTitle,
            reactionTimeTitleColor = reactionTimeTitleColor,
            reactionTimeAttemptColor = reactionTimeAttemptColor,
            reactionCheckMarkBackgroundColor = reactionCheckMarkBackgroundColor,
            reactionCheckMarkColor = reactionCheckMarkColor,
            reactionTimeMaximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
            reactionTimeMinimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
            reactionTimeNumberOfAttempts = reactionTimeNumberOfAttempts,
            reactionTimeTimeoutSeconds = reactionTimeTimeoutSeconds
        )

    }


    companion object {

        const val REACTION_TIME = "reaction_time"

        fun getReactionTimeCoreSteps(
            reactionTimeBackgroundColor: Int,
            reactionTimeTitle: String?,
            reactionTimeTitleColor: Int,
            reactionTimeAttemptColor: Int,
            reactionCheckMarkBackgroundColor: Int,
            reactionCheckMarkColor: Int,
            reactionTimeMaximumStimulusIntervalSeconds: Long,
            reactionTimeMinimumStimulusIntervalSeconds: Long,
            reactionTimeNumberOfAttempts: Int,
            reactionTimeTimeoutSeconds: Long
        ): List<Step> =

            listOf(
                ReactionTimeStep(
                    REACTION_TIME,
                    backgroundColor = reactionTimeBackgroundColor,
                    titleText = reactionTimeTitle,
                    titleTextColor = reactionTimeTitleColor,
                    attemptsTextColor = reactionTimeAttemptColor,
                    checkMarkBackgroundColor = reactionCheckMarkBackgroundColor,
                    checkMarkColor = reactionCheckMarkColor,
                    maximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
                    minimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
                    numberOfAttempts = reactionTimeNumberOfAttempts,
                    timeoutSeconds = reactionTimeTimeoutSeconds
                )
            )

    }

}