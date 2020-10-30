package com.fouryouandme.core.arch.deps.modules

import android.content.SharedPreferences
import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.auth.AuthApi
import com.squareup.moshi.Moshi

data class AuthModule(
    val api: AuthApi,
    val prefs: SharedPreferences,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val configurationModule: ConfigurationModule
)