package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.entity.consent.user.ConsentUser
import com.foryouandme.entity.optins.OptIns

interface ConsentRepository {

    suspend fun getConsentReview(token: String, studyId: String): ConsentReview?

    suspend fun getConsentInfo(token: String, studyId: String): ConsentInfo?

    suspend fun getOptIns(token: String, studyId: String): OptIns?

    suspend fun setPermission(token: String, permissionId: String, agree: Boolean)

    suspend fun getConsentUser(token: String, studyId: String): ConsentUser?

    suspend fun createUserConsent(token: String, studyId:String, email: String)

    suspend fun updateUserConsent(
        token: String,
        studyId: String,
        firstName: String,
        lastName: String,
        signatureBase64: String
    )

    suspend fun resendConfirmationEmail(token: String, email: String)

    suspend fun confirmEmail(token: String, studyId: String, code: String)

}