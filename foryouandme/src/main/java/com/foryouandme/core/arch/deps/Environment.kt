package com.foryouandme.core.arch.deps

abstract class Environment {

    abstract fun isDebuggable(): Boolean

    abstract fun isStaging(): Boolean

    abstract fun studyId(): String

    abstract fun getApiBaseUrl(): String

}