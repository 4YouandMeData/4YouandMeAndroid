package com.foryouandme.core.arch.deps.modules

import android.content.SharedPreferences
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.auth.network.AuthApi
import com.squareup.moshi.Moshi

data class AuthModule(
    val api: AuthApi,
    val prefs: SharedPreferences,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val configurationModule: ConfigurationModule,
    val analyticsModule: AnalyticsModule
)