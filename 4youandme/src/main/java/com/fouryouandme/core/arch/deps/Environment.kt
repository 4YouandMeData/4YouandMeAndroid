package com.fouryouandme.core.arch.deps

abstract class Environment {

    abstract fun isDebuggable(): Boolean

    abstract fun isStaging(): Boolean

    abstract fun studyId(): String

    // TODO: update for production
    fun getApiBaseUrl(): String =
        if (isStaging())
            "https://api-4youandme-staging.balzo.eu/"
        else
            "https://api-4youandme-staging.balzo.eu/"

}