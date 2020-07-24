package org.fouryouandme.core.data.api.common

import arrow.integrations.retrofit.adapter.CallK
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.common.request.AnswersRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AnswerApi {
    @POST("api/v1/questions/{question_id}/answer")
    fun sendAnswers(
        @Header(Headers.AUTH) token: String,
        @Path("question_id") questionId: String,
        @Body request: AnswersRequest
    ): CallK<Unit>
}