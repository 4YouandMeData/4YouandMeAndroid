package com.foryouandme.researchkit.result.reaction

import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.reaction.ReactionTimeTask

data class ReactionTimeResult(
    val attempts: List<ReactionTimeAttemptResult>
)

data class ReactionTimeAttemptResult(
    val deviceMotion: String,
    val timestamp: Long
)

fun TaskResult.toReactionTimeResult(): ReactionTimeResult {

    // parse all file result content

    val reactionTimeDeviceMotions =
        results.filterKeys { it.contains(ReactionTimeTask.REACTION_TIME_DEVICE_MOTION) }
            .mapNotNull { it.value as? FileResult }

    val reactionTimeAttemptResults =
        reactionTimeDeviceMotions
            .mapNotNull { it.toJsonResult() }
            .map { ReactionTimeAttemptResult(it.json, it.startDate.toInstant().toEpochMilli()) }


    return ReactionTimeResult(reactionTimeAttemptResults)

}