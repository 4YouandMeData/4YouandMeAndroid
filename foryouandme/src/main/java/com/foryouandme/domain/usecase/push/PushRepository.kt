package com.foryouandme.domain.usecase.push

interface PushRepository {

    suspend fun getPushToken(): String

}