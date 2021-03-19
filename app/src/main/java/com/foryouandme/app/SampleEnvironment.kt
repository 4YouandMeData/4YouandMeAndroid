package com.foryouandme.app

import android.content.Context
import com.foryouandme.data.datasource.Environment

class SampleEnvironment(private val context: Context) : Environment() {

    override fun isDebuggable(): Boolean = true

    override fun isStaging(): Boolean = true

    override fun studyId(): String =
        context.getString(R.string.STUDY_ID)

    override fun getApiBaseUrl(): String =
        context.getString(R.string.BASE_URL)

    override fun getOAuthBaseUrl(): String =
        context.getString(R.string.OAUTH_BASE_URL)

    override fun useCustomData(): Boolean = true

    override fun pinCodeSuffix(): String =
        context.getString(R.string.PIN_CODE_SUFFIX)

}