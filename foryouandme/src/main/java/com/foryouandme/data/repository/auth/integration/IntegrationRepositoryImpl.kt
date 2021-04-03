package com.foryouandme.data.repository.auth.integration

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.auth.integration.network.IntegrationApi
import com.foryouandme.domain.usecase.auth.integration.IntegrationRepository
import com.foryouandme.entity.integration.Integration
import javax.inject.Inject

class IntegrationRepositoryImpl @Inject constructor(
    private val api: IntegrationApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : IntegrationRepository {

    override suspend fun getIntegration(token: String, studyId: String): Integration? =
        authErrorInterceptor.execute { api.getIntegration(token, studyId) }
            .let { it.get().toIntegration(it) }

}