package com.foryouandme.data.repository.auth.screening

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.auth.screening.network.ScreeningApi
import com.foryouandme.domain.usecase.auth.screening.ScreeningRepository
import com.foryouandme.entity.screening.Screening
import javax.inject.Inject

class ScreeningRepositoryImpl @Inject constructor(
    private val api: ScreeningApi,
    private val authErrorInterceptor: AuthErrorInterceptor
): ScreeningRepository {

    override suspend fun getScreening(token: String, studyId: String): Screening? =
        authErrorInterceptor.execute { api.getScreening(token, studyId) }
            .let { it.get().toScreening(it) }

}