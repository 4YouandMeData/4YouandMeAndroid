package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.task.result.holepeg.HolePegResult
import javax.inject.Inject

class SendHolePegUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(
        taskId: String,
        holePegResult: HolePegResult
    ) {

        repository.updateHolePegTask(
            getTokenUseCase(),
            taskId,
            holePegResult
        )

    }

}