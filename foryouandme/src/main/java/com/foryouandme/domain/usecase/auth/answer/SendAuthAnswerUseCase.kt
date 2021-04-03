package com.foryouandme.domain.usecase.auth.answer

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class SendAuthAnswerUseCase @Inject constructor(
    private val repository: AuthAnswerRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    ) {
        repository.sendAnswer(
            getTokenUseCase(),
            environment.studyId(),
            questionId,
            answerText,
            possibleAnswerId
        )
    }

}