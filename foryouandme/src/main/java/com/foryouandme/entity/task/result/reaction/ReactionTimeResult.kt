package com.foryouandme.entity.task.result.reaction

import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.StepResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.reaction.ReactionTimeTask
import org.threeten.bp.ZonedDateTime

class ReactionTimeResult(
    identifier: String,
    startDate: ZonedDateTime,
    val attempts: List<FileResult>,
    endDate: ZonedDateTime
) : StepResult(identifier, startDate, endDate)

fun TaskResult.toReactionTimeResult(): ReactionTimeResult? {

    // parse all file result content
    return (results[ReactionTimeTask.REACTION_TIME] as? ReactionTimeResult)

}