package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.entity.consent.review.ConsentReview

interface ConsentRepository {

    suspend fun getConsentReview(token: String, studyId: String): ConsentReview?

}