package com.foryouandme.domain.usecase.usersettings

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.usersettings.UserSettings
import javax.inject.Inject

class UpdateUserSettingsUseCase @Inject constructor(
    private val repository: UserSettingsRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(userSettings: UserSettings) =
        repository.updateUserSettings(getTokenUseCase(), userSettings)

}