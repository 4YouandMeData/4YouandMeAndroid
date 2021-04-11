package com.foryouandme.data.repository.auth.consent

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.auth.consent.network.ConsentApi
import com.foryouandme.data.repository.auth.consent.network.request.OptInPermissionRequest
import com.foryouandme.domain.usecase.auth.consent.ConsentRepository
import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.entity.consent.review.ConsentReview
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

}