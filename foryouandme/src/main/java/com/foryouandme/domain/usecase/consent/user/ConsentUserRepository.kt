package com.foryouandme.domain.usecase.consent.user

interface ConsentUserRepository {

    suspend fun completeConsent(token: String, studyId: String)

}