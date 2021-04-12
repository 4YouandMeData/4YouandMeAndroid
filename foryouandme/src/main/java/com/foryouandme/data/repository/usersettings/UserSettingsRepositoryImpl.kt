package com.foryouandme.data.repository.usersettings

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.usersettings.network.UserSettingsApi
import com.foryouandme.domain.usecase.usersettings.UserSettingsRepository
import com.foryouandme.entity.usersettings.UserSettings
import javax.inject.Inject

class UserSettingsRepositoryImpl @Inject constructor(
    private val api: UserSettingsApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : UserSettingsRepository {

    override suspend fun getUserSettings(token: String): UserSettings? =
        authErrorInterceptor.execute {
            api.getUserSettings(token)
        }
            .toUserSettings()

//    override suspend fun updateUserSettings(
//        token: String,
//        userSettingsUpdateRequest: UserSettingsUpdateRequest
//    ) {
//        authErrorInterceptor.execute {
//            api.updateUserSettings(token, userSettingsUpdateRequest)
//        }
//    }

}