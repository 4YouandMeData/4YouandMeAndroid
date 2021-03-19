package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.task.result.reaction.ReactionTimeResult
import javax.inject.Inject

class SendReactionTimeUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(
        taskId: String,
        reactionTimeResult: ReactionTimeResult
    ) {

        repository.updateReactionTimeTask(
            getTokenUseCase(),
            taskId,
            reactionTimeResult
        )

    }

}