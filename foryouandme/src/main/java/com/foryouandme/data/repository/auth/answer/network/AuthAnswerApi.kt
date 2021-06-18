package com.foryouandme.data.repository.auth.answer.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.answer.network.request.AnswersRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthAnswerApi {

    @POST("api/v1/questions/{question_id}/answer")
    suspend fun sendAnswers(
        @Header(Headers.AUTH) token: String,
        @Path("question_id") questionId: String,
        @Body request: AnswersRequest
    )

}