package com.foryouandme.domain.usecase.permission

import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult

interface PermissionRepository {

    fun isPermissionGranted(permission: Permission): Boolean

    suspend fun requestPermission(permission: Permission): PermissionResult

    suspend fun requestPermissions(permissions: List<Permission>): List<PermissionResult>

}