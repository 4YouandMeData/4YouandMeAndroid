package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.consent.review.ConsentReview
import javax.inject.Inject

class GetConsentReviewUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(): ConsentReview? =
        repository.getConsentReview(getTokenUseCase(), environment.studyId())

}