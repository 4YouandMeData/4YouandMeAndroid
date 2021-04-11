package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.entity.optins.OptIns

interface ConsentRepository {

    suspend fun getConsentReview(token: String, studyId: String): ConsentReview?

    suspend fun getConsentInfo(token: String, studyId: String): ConsentInfo?

    suspend fun getOptIns(token: String, studyId: String): OptIns?

    suspend fun setPermission(token: String, permissionId: String, agree: Boolean)

}