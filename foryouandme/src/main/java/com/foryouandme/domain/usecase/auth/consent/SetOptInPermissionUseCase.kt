package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class SetOptInPermissionUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(permissionId: String, agree: Boolean) {
        repository.setPermission(getTokenUseCase(), permissionId, agree)
    }

}