package com.foryouandme.domain.usecase.auth.integration

import com.foryouandme.entity.integration.Integration

interface IntegrationRepository {

    suspend fun getIntegration(token: String, studyId: String): Integration?

}