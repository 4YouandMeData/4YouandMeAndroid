package com.foryouandme.domain.usecase.usersettings

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: UserSettingsRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke() =
        repository.getUserSettings(getTokenUseCase())

}