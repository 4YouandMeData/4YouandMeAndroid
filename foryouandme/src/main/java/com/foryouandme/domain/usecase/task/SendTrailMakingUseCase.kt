package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.task.result.trailmaking.TrailMakingResult
import javax.inject.Inject

class SendTrailMakingUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(
        taskId: String,
        trailMakingResult: TrailMakingResult
    ) {

        repository.updateTrailMakingTask(
            getTokenUseCase(),
            taskId,
            trailMakingResult
        )

    }

}