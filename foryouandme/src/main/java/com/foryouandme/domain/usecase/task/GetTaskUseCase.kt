package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.task.Task
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(taskId: String): Task? =
        repository.getTask(getTokenUseCase(), taskId)

}