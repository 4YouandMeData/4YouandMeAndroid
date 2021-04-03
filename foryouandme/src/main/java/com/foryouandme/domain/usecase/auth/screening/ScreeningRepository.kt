package com.foryouandme.domain.usecase.auth.screening

import com.foryouandme.entity.screening.Screening

interface ScreeningRepository {

    suspend fun getScreening(token: String, studyId: String): Screening?

}