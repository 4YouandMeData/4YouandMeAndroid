package com.foryouandme.data.repository.auth.answer

import com.foryouandme.core.data.api.common.request.AnswerDataRequest
import com.foryouandme.core.data.api.common.request.AnswersRequest
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.ext.getTimestampUTC
import com.foryouandme.data.repository.auth.answer.network.AuthAnswerApi
import com.foryouandme.domain.usecase.auth.answer.AuthAnswerRepository
import javax.inject.Inject

class AuthAnswerRepositoryImpl @Inject constructor(
    private val api: AuthAnswerApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : AuthAnswerRepository {

    override suspend fun sendAnswer(
        token: String,
        studyId: String,
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    ) {
        authErrorInterceptor.execute {
            api.sendAnswers(
                token,
                questionId,
                AnswersRequest(
                    AnswerDataRequest(
                        answerText,
                        getTimestampUTC(),
                        possibleAnswerId
                    )
                )
            )
        }
    }

}