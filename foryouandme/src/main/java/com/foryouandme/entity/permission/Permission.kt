package com.foryouandme.entity.permission

sealed class Permission {

    object Camera : Permission()
    object RecordAudio : Permission()
    object Location : Permission()

    companion object

}

sealed class PermissionResult(val permission: Permission) {

    class Granted(permission: Permission) : PermissionResult(permission)
    class Denied(
        permission: Permission,
        val isPermanentlyDenied: Boolean
    ) : PermissionResult(permission)

}