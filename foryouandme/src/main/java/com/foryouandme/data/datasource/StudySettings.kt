package com.foryouandme.data.datasource

abstract class StudySettings {

    abstract val isDebuggable: Boolean

    abstract val isStaging: Boolean

    abstract val studyId: String

    abstract val getApiBaseUrl: String

    abstract val getOAuthBaseUrl: String

    abstract val useCustomData: Boolean

    open val pinCodeSuffix: String = ""

    abstract val isLocationPermissionEnabled: Boolean

}