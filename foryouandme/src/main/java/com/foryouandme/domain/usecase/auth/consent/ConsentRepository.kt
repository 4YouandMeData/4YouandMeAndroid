package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.entity.consent.review.ConsentReview

interface ConsentRepository {

    suspend fun getConsentReview(token: String, studyId: String): ConsentReview?

    suspend fun getConsentInfo(token: String, studyId: String): ConsentInfo?

}