package com.foryouandme.domain.usecase.permission

import com.foryouandme.entity.permission.Permission
import javax.inject.Inject

class IsPermissionGrantedUseCase @Inject constructor(
    private val repository: PermissionRepository
) {

    operator fun invoke(permission: Permission): Boolean =
        repository.isPermissionGranted(permission)

}