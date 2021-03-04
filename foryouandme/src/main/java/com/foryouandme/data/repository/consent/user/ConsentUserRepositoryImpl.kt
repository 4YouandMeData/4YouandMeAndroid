package com.foryouandme.data.repository.consent.user

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.consent.user.network.ConsentUserApi
import com.foryouandme.data.repository.consent.user.network.request.CompleteUserConsentRequest
import com.foryouandme.data.repository.consent.user.network.request.UserConsentRequest
import com.foryouandme.domain.usecase.consent.user.ConsentUserRepository
import javax.inject.Inject

class ConsentUserRepositoryImpl @Inject constructor(
    private val api: ConsentUserApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : ConsentUserRepository {

    override suspend fun completeConsent(token: String, studyId: String) {
        authErrorInterceptor.execute {
            api.completeConsent(
                token,
                studyId,
                UserConsentRequest(CompleteUserConsentRequest.build())
            )
        }
    }

}