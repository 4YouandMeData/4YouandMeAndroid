package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import com.foryouandme.researchkit.result.reaction.ReactionTimeResult
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