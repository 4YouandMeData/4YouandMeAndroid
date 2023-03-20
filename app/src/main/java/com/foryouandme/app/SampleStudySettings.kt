package com.foryouandme.app

import android.content.Context
import com.foryouandme.data.datasource.StudySettings

class SampleStudySettings(context: Context) : StudySettings() {

    override val isDebuggable: Boolean = true

    override val isStaging: Boolean = true

    override val studyId: String =
        context.getString(R.string.STUDY_ID)

    override val getApiBaseUrl: String =
        context.getString(R.string.BASE_URL)

    override val getOAuthBaseUrl: String =
        context.getString(R.string.OAUTH_BASE_URL)

    override val useCustomData: Boolean = true

    override val pinCodeSuffix: String =
        context.getString(R.string.PIN_CODE_SUFFIX)

    override val isLocationPermissionEnabled: Boolean = true
}