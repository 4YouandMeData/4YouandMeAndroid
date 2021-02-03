package com.foryouandme.domain.usecase.survey

import com.foryouandme.entity.survey.Survey

interface SurveyRepository {

    suspend fun getSurvey(token: String, surveyId: String): Survey?

}