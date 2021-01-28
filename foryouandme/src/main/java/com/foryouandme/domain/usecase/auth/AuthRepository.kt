package com.foryouandme.domain.usecase.auth

interface AuthRepository {

    suspend fun getToken(): String

}