package com.foryouandme.app

import android.app.Application
import com.foryouandme.core.arch.deps.Environment

class SampleEnvironment(private val application: Application) : Environment() {

    override fun isDebuggable(): Boolean = true

    override fun isStaging(): Boolean = true

    override fun studyId(): String =
        application.getString(R.string.STUDY_ID)

    override fun getApiBaseUrl(): String =
        application.getString(R.string.BASE_URL)
}