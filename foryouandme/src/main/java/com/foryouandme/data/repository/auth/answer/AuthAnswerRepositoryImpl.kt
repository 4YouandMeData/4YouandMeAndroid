package com.foryouandme.data.repository.auth.answer

import com.foryouandme.domain.usecase.auth.answer.AuthAnswerRepository
import javax.inject.Inject

class AuthAnswerRepositoryImpl @Inject constructor(

): AuthAnswerRepository {

    override suspend fun sendAnswer(
        token: String,
        studyId: String,
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    ) {
        TODO("Not yet implemented")
    }

}