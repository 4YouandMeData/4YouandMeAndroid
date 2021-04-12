package com.foryouandme.domain.usecase.usersettings

import com.foryouandme.entity.usersettings.UserSettings

interface UserSettingsRepository {

    suspend fun getUserSettings(token: String): UserSettings?

    //suspend fun updateUserSettings(token: String, userSettingsUpdateRequest: UserSettingsUpdateRequest)

}