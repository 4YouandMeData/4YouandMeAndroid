package com.foryouandme.data.repository.survey

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.survey.network.SurveyApi
import com.foryouandme.domain.usecase.survey.SurveyRepository
import com.foryouandme.entity.survey.Survey
import javax.inject.Inject

class SurveyRepositoryImpl @Inject constructor(
    private val api: SurveyApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : SurveyRepository {

    override suspend fun getSurvey(token: String, surveyId: String): Survey? =
        authErrorInterceptor.execute { api.getSurvey(token, surveyId) }.toSurvey()

}