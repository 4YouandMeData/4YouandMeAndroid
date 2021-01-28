package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import javax.inject.Inject

class SubmitQuickActivityAnswer @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(taskId: String, answerId: Int) {

        repository.updateQuickActivity(getTokenUseCase(), taskId, answerId)

    }

}