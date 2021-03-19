package com.foryouandme.data.datasource

abstract class Environment {

    abstract fun isDebuggable(): Boolean

    abstract fun isStaging(): Boolean

    abstract fun studyId(): String

    abstract fun getApiBaseUrl(): String

    abstract fun getOAuthBaseUrl(): String

    abstract fun useCustomData(): Boolean

    open fun pinCodeSuffix(): String = ""

}