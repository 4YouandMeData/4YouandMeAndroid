package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.survey.SurveyAnswerUpdate
import javax.inject.Inject

class SendSurveyTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(
        taskId: String,
        answers: List<SurveyAnswerUpdate>
    ) {

        repository.updateSurvey(
            getTokenUseCase(),
            taskId,
            answers
        )

    }

}