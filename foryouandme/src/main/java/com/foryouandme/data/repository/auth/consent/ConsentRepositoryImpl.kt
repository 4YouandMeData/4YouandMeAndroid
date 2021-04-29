package com.foryouandme.data.repository.auth.consent

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.ext.unwrapResponse
import com.foryouandme.data.repository.auth.consent.network.ConsentApi
import com.foryouandme.data.repository.consent.user.network.request.*
import com.foryouandme.domain.usecase.auth.consent.ConsentRepository
import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.entity.consent.user.ConsentUser
import com.foryouandme.entity.optins.OptIns
import javax.inject.Inject

class ConsentRepositoryImpl @Inject constructor(
    private val api: ConsentApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : ConsentRepository {

    override suspend fun getConsentReview(token: String, studyId: String): ConsentReview? =
        authErrorInterceptor.execute { api.getConsentReview(token, studyId) }
            .let { it.get().toConsentReview(it) }

    override suspend fun getConsentInfo(token: String, studyId: String): ConsentInfo? =
        authErrorInterceptor.execute { api.getConsentInfo(token, studyId) }
            .let { it.get().toConsentInfo(it) }

    override suspend fun getOptIns(token: String, studyId: String): OptIns? =
        authErrorInterceptor.execute { api.getOptIns(token, studyId) }
            .let { it.get().toOptIns(it) }

    override suspend fun setPermission(token: String, permissionId: String, agree: Boolean) {
        authErrorInterceptor.execute {
            api.setPermission(token, permissionId, OptInPermissionRequest.build(agree))
        }
    }

    /* --- user --- */

    override suspend fun getConsentUser(token: String, studyId: String): ConsentUser? =
        authErrorInterceptor.execute { api.getConsentUser(token, studyId) }
            .let { it.get().toConsentUser(it) }

    override suspend fun createUserConsent(token: String, studyId: String, email: String) {
        val request = UserConsentRequest(CreateUserConsentRequest(true, email))
        authErrorInterceptor.execute { api.createUserConsent(token, studyId, request) }
    }

    override suspend fun updateUserConsent(
        token: String,
        studyId: String,
        firstName: String,
        lastName: String,
        signatureBase64: String
    ) {
        val request =
            UserConsentRequest(
                UpdateUserConsentRequest(
                    firstName,
                    lastName,
                    "data:image/png;base64,\\$signatureBase64"
                )
            )
        authErrorInterceptor.execute { api.updateUserConsent(token, studyId, request) }
    }

    override suspend fun resendConfirmationEmail(token: String, email: String) {
        authErrorInterceptor.execute { api.resendConfirmationEmail(token, email) }.unwrapResponse()
    }

    override suspend fun confirmEmail(token: String, studyId: String, code: String) {
        val request =
            UserConsentRequest(
                ConfirmUserConsentEmailRequest(code)
            )
        authErrorInterceptor.execute { api.confirmEmail(token, studyId, request) }.unwrapResponse()
    }

}