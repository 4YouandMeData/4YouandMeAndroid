package com.foryouandme.domain.usecase.auth.answer

interface AuthAnswerRepository {

    suspend fun sendAnswer(
        token: String,
        studyId: String,
        questionId: String,
        answerText: String,
        possibleAnswerId: String
    )

}