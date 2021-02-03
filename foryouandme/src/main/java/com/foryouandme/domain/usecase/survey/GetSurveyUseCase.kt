package com.foryouandme.domain.usecase.survey

import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import com.foryouandme.entity.survey.Survey
import javax.inject.Inject

class GetSurveyUseCase @Inject constructor(
    private val repository: SurveyRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(surveyId: String): Survey? =
        repository.getSurvey(getTokenUseCase(), surveyId)

}