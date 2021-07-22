package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.consent.review.ConsentReview
import javax.inject.Inject

class GetConsentReviewUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(): ConsentReview? =
        repository.getConsentReview(getTokenUseCase(), settings.studyId)

}